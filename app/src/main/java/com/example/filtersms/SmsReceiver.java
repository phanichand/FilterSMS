package com.example.filtersms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

import androidx.room.Room;

import com.example.filtersms.data.AppDatabase;
import com.example.filtersms.data.LogEntry;
import com.example.filtersms.data.LogEntryDao;
import com.example.filtersms.data.SmsFilterRule;
import com.example.filtersms.data.SmsFilterRuleDao;
import com.example.filtersms.data.Migration1To2;
import com.example.filtersms.data.Migration2To3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";
    private static final String PREFS_NAME = "FilterSmsPrefs";
    private static final String KEY_RECIPIENT_EMAIL = "recipientEmail";
    private static final String KEY_SMTP_USERNAME = "smtpUsername";
    private static final String KEY_SMTP_PASSWORD = "smtpPassword";
    private static final String KEY_SMTP_HOST = "smtpHost";
    private static final String KEY_SMTP_PORT = "smtpPort";
    private static final String KEY_MESSAGES_LISTENED = "messagesListened";
    private static final String KEY_MESSAGES_FILTERED = "messagesFiltered";
    private static final String KEY_EMAILS_SENT = "emailsSent";

    private AppDatabase db;
    private SmsFilterRuleDao smsFilterRuleDao;
    private LogEntryDao logEntryDao;
    private ExecutorService executorService;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            final PendingResult pendingResult = goAsync();
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            final String recipientEmail = sharedPreferences.getString(KEY_RECIPIENT_EMAIL, "");
            final String smtpUsername = sharedPreferences.getString(KEY_SMTP_USERNAME, "");
            final String smtpPassword = sharedPreferences.getString(KEY_SMTP_PASSWORD, "");
            final String smtpHost = sharedPreferences.getString(KEY_SMTP_HOST, "smtp.gmail.com");
            final String smtpPort = sharedPreferences.getString(KEY_SMTP_PORT, "465");

            // Initialize Room database and DAO
            db = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "sms-filter-db")
                    .addMigrations(Migration1To2.MIGRATION_1_2, Migration2To3.MIGRATION_2_3, AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5)
                    .build();
            smsFilterRuleDao = db.smsFilterRuleDao();
            logEntryDao = db.logEntryDao();
            executorService = Executors.newSingleThreadExecutor();

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<SmsFilterRule> rules = smsFilterRuleDao.getAllRules();

                        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                        if (messages != null && messages.length > 0) {
                            StringBuilder messageBody = new StringBuilder();
                            for (SmsMessage smsMessage : messages) {
                                messageBody.append(smsMessage.getMessageBody());
                            }

                            String sender = messages[0].getDisplayOriginatingAddress();
                            String fullMessage = messageBody.toString();

                            log("Read incoming message from " + sender + ": " + fullMessage);
                            incrementCounter(sharedPreferences, KEY_MESSAGES_LISTENED);

                            SmsFilterRule matchingRule = getMatchingRule(sender, fullMessage, rules);
                            if (matchingRule != null) {
                                log("Message from " + sender + " passed filter. Sending email.");
                                incrementCounter(sharedPreferences, KEY_MESSAGES_FILTERED);
                                if (!recipientEmail.isEmpty() && !smtpUsername.isEmpty() && !smtpPassword.isEmpty() && !smtpHost.isEmpty() && !smtpPort.isEmpty()) {
                                    String emailSubject = sender;
                                    EmailSender.sendEmail(smtpUsername, smtpPassword, smtpHost, smtpPort, recipientEmail, emailSubject, fullMessage, new EmailSender.Callback() {
                                        @Override
                                        public void onEmailSent(boolean success) {
                                            if (success) {
                                                log("Email sent successfully for message from " + sender);
                                                incrementCounter(sharedPreferences, KEY_EMAILS_SENT);
                                            } else {
                                                log("Failed to send email for message from " + sender);
                                            }
                                        }
                                    });
                                } else {
                                    log("Email sending skipped for message from " + sender + ": Email not configured.");
                                }

                                if (matchingRule.isSendToGoogleSheet()) {
                                    List<Object> data = new ArrayList<>();
                                    data.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                                    data.add(sender);
                                    data.add(fullMessage);
                                    GoogleSheetsService.writeToSheet(context, matchingRule.getSheetId(), matchingRule.getSheetName(), data);
                                }

                                if (matchingRule.isSendToWebhook()) {
                                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                                    WebhookService.sendWebhook(matchingRule.getWebhookUrl(), sender, fullMessage, timestamp);
                                }
                            } else {
                                log("Message from " + sender + " did not pass filter.");
                            }
                        }
                    } finally {
                        // Shutdown the executor after processing to prevent memory leaks
                        executorService.shutdown();
                        pendingResult.finish();
                    }
                }
            });
        }
    }

    private void log(String message) {
        Log.d(TAG, message);
        if (logEntryDao != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String timestamp = sdf.format(new Date());
            logEntryDao.insert(new LogEntry(timestamp, message));
        }
    }

    private SmsFilterRule getMatchingRule(String sender, String messageBody, List<SmsFilterRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return null; // No rules configured, so no filtering
        }

        for (SmsFilterRule rule : rules) {
            boolean senderMatches = true; // Default to true, will be set to false if sender doesn't match
            if (rule.getSender() != null && !rule.getSender().trim().isEmpty()) {
                if (!sender.toLowerCase().contains(rule.getSender().trim().toLowerCase())) {
                    senderMatches = false;
                }
            }

            // If sender doesn't match, no need to check the message pattern. Continue to the next rule.
            if (!senderMatches) {
                continue;
            }

            boolean messageMatches = false;
            if (rule.getMessagePattern() != null && !rule.getMessagePattern().trim().isEmpty()) {
                try {
                    Pattern pattern = Pattern.compile(rule.getMessagePattern().trim());
                    Matcher matcher = pattern.matcher(messageBody);
                    if (matcher.find()) {
                        messageMatches = true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Invalid regex in rule: " + rule.getMessagePattern(), e);
                }
            } else {
                messageMatches = true; // If rule has no message pattern, consider it a match
            }

            if (messageMatches) {
                return rule; // This SMS matches the rule
            }
        }
        return null; // No rule matched this SMS
    }

    private void incrementCounter(SharedPreferences sharedPreferences, String key) {
        int count = sharedPreferences.getInt(key, 0);
        sharedPreferences.edit().putInt(key, count + 1).apply();
    }
}