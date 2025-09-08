package com.example.filtersms;

import android.util.Log;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private static final String TAG = "EmailSender";
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface Callback {
        void onEmailSent(boolean success);
    }

    public static void sendEmail(final String senderEmail, final String senderPassword, final String smtpHost, final String smtpPort, final String recipientEmail, final String subject, final String body, final Callback callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Properties props = new Properties();
                props.put("mail.smtp.host", smtpHost);
                props.put("mail.smtp.socketFactory.port", smtpPort);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", smtpPort);
                props.put("mail.smtp.starttls.enable", "true");

                Log.d(TAG, "Email properties: " + props);

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(senderEmail));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                    message.setSubject(subject);
                    message.setText(body);

                    Transport.send(message);
                    Log.d(TAG, "Email sent successfully to " + recipientEmail);
                    if (callback != null) {
                        callback.onEmailSent(true);
                    }
                } catch (MessagingException e) {
                    Log.e(TAG, "Error sending email to " + recipientEmail + ": " + e.getMessage(), e);
                    if (callback != null) {
                        callback.onEmailSent(false);
                    }
                }
            }
        });
    }
}