package com.example.filtersms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

public class EmailSettingsActivity extends AppCompatActivity {

    private EditText editTextRecipientEmail;
    private EditText editTextSmtpUsername;
    private EditText editTextSmtpPassword;
    private EditText editTextSmtpHost;
    private EditText editTextSmtpPort;
    private Button buttonSaveEmailSettings;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FilterSmsPrefs";
    private static final String KEY_RECIPIENT_EMAIL = "recipientEmail";
    private static final String KEY_SMTP_USERNAME = "smtpUsername";
    private static final String KEY_SMTP_PASSWORD = "smtpPassword";
    private static final String KEY_SMTP_HOST = "smtpHost";
    private static final String KEY_SMTP_PORT = "smtpPort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Email Settings");
        }

        editTextRecipientEmail = findViewById(R.id.editTextRecipientEmail);
        editTextSmtpUsername = findViewById(R.id.editTextSmtpUsername);
        editTextSmtpPassword = findViewById(R.id.editTextSmtpPassword);
        editTextSmtpHost = findViewById(R.id.editTextSmtpHost);
        editTextSmtpPort = findViewById(R.id.editTextSmtpPort);
        buttonSaveEmailSettings = findViewById(R.id.buttonSaveEmailSettings);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        loadEmailSettings();

        buttonSaveEmailSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEmailSettings();
            }
        });

        Button buttonSendTestEmail = findViewById(R.id.buttonSendTestEmail);
        buttonSendTestEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EmailSettingsActivity", "Send Test Email button clicked.");
                sendTestEmail();
            }
        });
    }

    private void loadEmailSettings() {
        String recipientEmail = sharedPreferences.getString(KEY_RECIPIENT_EMAIL, "");
        String smtpUsername = sharedPreferences.getString(KEY_SMTP_USERNAME, "");
        String smtpPassword = sharedPreferences.getString(KEY_SMTP_PASSWORD, "");
        String smtpHost = sharedPreferences.getString(KEY_SMTP_HOST, "smtp.gmail.com");
        String smtpPort = sharedPreferences.getString(KEY_SMTP_PORT, "587");

        editTextRecipientEmail.setText(recipientEmail);
        editTextSmtpUsername.setText(smtpUsername);
        editTextSmtpPassword.setText(smtpPassword);
        editTextSmtpHost.setText(smtpHost);
        editTextSmtpPort.setText(smtpPort);
    }

    private void saveEmailSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RECIPIENT_EMAIL, editTextRecipientEmail.getText().toString());
        editor.putString(KEY_SMTP_USERNAME, editTextSmtpUsername.getText().toString());
        editor.putString(KEY_SMTP_PASSWORD, editTextSmtpPassword.getText().toString());
        editor.putString(KEY_SMTP_HOST, editTextSmtpHost.getText().toString());
        editor.putString(KEY_SMTP_PORT, editTextSmtpPort.getText().toString());
        editor.apply();

        Toast.makeText(this, "Email Settings saved!", Toast.LENGTH_SHORT).show();
    }

    private void sendTestEmail() {
        Log.d("EmailSettingsActivity", "sendTestEmail() method called.");
        String recipientEmail = editTextRecipientEmail.getText().toString();
        String smtpUsername = editTextSmtpUsername.getText().toString();
        String smtpPassword = editTextSmtpPassword.getText().toString();
        String smtpHost = editTextSmtpHost.getText().toString();
        String smtpPort = editTextSmtpPort.getText().toString();

        if (recipientEmail.isEmpty() || smtpUsername.isEmpty() || smtpPassword.isEmpty() || smtpHost.isEmpty() || smtpPort.isEmpty()) {
            Log.e("EmailSettingsActivity", "Email settings are incomplete.");
            Toast.makeText(this, "Please fill in all email settings", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("EmailSettingsActivity", "Attempting to send test email to: " + recipientEmail);
        String subject = "Test Email from FilterSMS App";
        String body = "This is a test email to verify your SMTP settings.";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending test email...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        EmailSender.sendEmail(smtpUsername, smtpPassword, smtpHost, smtpPort, recipientEmail, subject, body, new EmailSender.Callback() {
            @Override
            public void onEmailSent(boolean success) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (success) {
                            Toast.makeText(EmailSettingsActivity.this, "Test email sent successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EmailSettingsActivity.this, "Failed to send test email. Check logs for details.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}