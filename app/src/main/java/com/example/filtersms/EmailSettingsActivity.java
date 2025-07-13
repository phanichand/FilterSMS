package com.example.filtersms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Added import
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem; // Added import
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

public class EmailSettingsActivity extends AppCompatActivity {

    private EditText editTextRecipientEmail;
    private EditText editTextSmtpUsername;
    private EditText editTextSmtpPassword;
    private Button buttonSaveEmailSettings;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FilterSmsPrefs";
    private static final String KEY_RECIPIENT_EMAIL = "recipientEmail";
    private static final String KEY_SMTP_USERNAME = "smtpUsername";
    private static final String KEY_SMTP_PASSWORD = "smtpPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_settings);

        Toolbar toolbar = findViewById(R.id.toolbar); // Initialize Toolbar
        setSupportActionBar(toolbar); // Set as ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button
            getSupportActionBar().setDisplayShowHomeEnabled(true); // Show back button
            getSupportActionBar().setTitle("Email Settings"); // Set title
        }

        editTextRecipientEmail = findViewById(R.id.editTextRecipientEmail);
        editTextSmtpUsername = findViewById(R.id.editTextSmtpUsername);
        editTextSmtpPassword = findViewById(R.id.editTextSmtpPassword);
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

        editTextRecipientEmail.setText(recipientEmail);
        editTextSmtpUsername.setText(smtpUsername);
        editTextSmtpPassword.setText(smtpPassword);
    }

    private void saveEmailSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RECIPIENT_EMAIL, editTextRecipientEmail.getText().toString());
        editor.putString(KEY_SMTP_USERNAME, editTextSmtpUsername.getText().toString());
        editor.putString(KEY_SMTP_PASSWORD, editTextSmtpPassword.getText().toString());
        editor.apply();

        Toast.makeText(this, "Email Settings saved!", Toast.LENGTH_SHORT).show();
    }

    private void sendTestEmail() {
        Log.d("EmailSettingsActivity", "sendTestEmail() method called.");
        String recipientEmail = editTextRecipientEmail.getText().toString();
        String smtpUsername = editTextSmtpUsername.getText().toString();
        String smtpPassword = editTextSmtpPassword.getText().toString();

        if (recipientEmail.isEmpty() || smtpUsername.isEmpty() || smtpPassword.isEmpty()) {
            Log.e("EmailSettingsActivity", "Email settings are incomplete.");
            Toast.makeText(this, "Please fill in all email settings", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("EmailSettingsActivity", "Attempting to send test email to: " + recipientEmail);
        String subject = "Test Email from FilterSMS App";
        String body = "This is a test email to verify your SMTP settings.";

        EmailSender.sendEmail(smtpUsername, smtpPassword, recipientEmail, subject, body);

        Toast.makeText(this, "Sending test email...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and go back to the previous one
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}