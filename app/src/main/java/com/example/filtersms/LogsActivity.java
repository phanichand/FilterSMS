package com.example.filtersms;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        TextView textViewLogs = findViewById(R.id.textViewLogs);
        textViewLogs.setMovementMethod(new ScrollingMovementMethod());

        try {
            Process process = Runtime.getRuntime().exec("logcat -d -s SmsReceiver");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append("\n");
            }
            textViewLogs.setText(log.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}