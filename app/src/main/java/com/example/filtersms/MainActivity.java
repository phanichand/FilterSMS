package com.example.filtersms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filtersms.data.AppDatabase;
import com.example.filtersms.data.SmsFilterRule;
import com.example.filtersms.data.SmsFilterRuleDao;
import com.example.filtersms.data.Migration1To2;
import com.example.filtersms.data.Migration2To3;
import com.example.filtersms.ui.RuleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.app.Activity;
import android.net.Uri;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    private RecyclerView recyclerViewRules;
    private RuleAdapter ruleAdapter;
    private FloatingActionButton fabAddRule;
    private Button buttonEmailSettings;
    private TextView textMessagesListened;
    private TextView textMessagesFiltered;
    private TextView textEmailsSent;

    private AppDatabase db;
    private SmsFilterRuleDao smsFilterRuleDao;
    private ExecutorService executorService;

    public static final String EXTRA_RULE_ID = "com.example.filtersms.EXTRA_RULE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!hasSmsPermissions()) {
            requestSmsPermissions();
        }

        recyclerViewRules = findViewById(R.id.recyclerViewRules);
        recyclerViewRules.setLayoutManager(new LinearLayoutManager(this));
        ruleAdapter = new RuleAdapter();
        recyclerViewRules.setAdapter(ruleAdapter);

        fabAddRule = findViewById(R.id.fabAddRule);
        buttonEmailSettings = findViewById(R.id.buttonEmailSettings);
        textMessagesListened = findViewById(R.id.text_messages_listened);
        textMessagesFiltered = findViewById(R.id.text_messages_filtered);
        textEmailsSent = findViewById(R.id.text_emails_sent);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "sms-filter-db")
                .addMigrations(Migration1To2.MIGRATION_1_2, Migration2To3.MIGRATION_2_3)
                .build();
        smsFilterRuleDao = db.smsFilterRuleDao();
        executorService = Executors.newSingleThreadExecutor();

        fabAddRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditRuleActivity.class);
                startActivity(intent);
            }
        });

        buttonEmailSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmailSettingsActivity.class);
                startActivity(intent);
            }
        });

        ruleAdapter.setOnItemClickListener(new RuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SmsFilterRule rule) {
                Intent intent = new Intent(MainActivity.this, AddEditRuleActivity.class);
                intent.putExtra(EXTRA_RULE_ID, rule.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(SmsFilterRule rule) {
                deleteRule(rule);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_logs) {
            Intent intent = new Intent(this, LogsActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_backup) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/json");
            intent.putExtra(Intent.EXTRA_TITLE, "sms_filter_rules.json");
            createDocumentLauncher.launch(intent);
            return true;
        } else if (itemId == R.id.action_restore) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/json");
            openDocumentLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasSmsPermissions()) {
            loadRules();
            loadStats();
        } else {
            requestSmsPermissions();
        }
    }

    private void loadRules() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<SmsFilterRule> rules = smsFilterRuleDao.getAllRules();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ruleAdapter.setRules(rules);
                    }
                });
            }
        });
    }

    private void loadStats() {
        SharedPreferences sharedPreferences = getSharedPreferences("FilterSmsPrefs", MODE_PRIVATE);
        int messagesListened = sharedPreferences.getInt("messagesListened", 0);
        int messagesFiltered = sharedPreferences.getInt("messagesFiltered", 0);
        int emailsSent = sharedPreferences.getInt("emailsSent", 0);

        textMessagesListened.setText("Messages Listened: " + messagesListened);
        textMessagesFiltered.setText("Messages Filtered: " + messagesFiltered);
        textEmailsSent.setText("Emails Sent: " + emailsSent);
    }

    private void deleteRule(final SmsFilterRule rule) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                smsFilterRuleDao.deleteRule(rule);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Rule deleted!", Toast.LENGTH_SHORT).show();
                        loadRules(); // Refresh the list
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private boolean hasSmsPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSmsPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                SMS_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
            } else {
                Toast.makeText(this, "SMS permissions are required to filter messages. Please grant the permissions in the app settings.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private final ActivityResultLauncher<Intent> createDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        backupRules(uri);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> openDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        restoreRules(uri);
                    }
                }
            });

    private void backupRules(Uri uri) {
        executorService.execute(() -> {
            List<SmsFilterRule> rules = smsFilterRuleDao.getAllRules();
            Gson gson = new Gson();
            String json = gson.toJson(rules);

            try (OutputStream os = getContentResolver().openOutputStream(uri)) {
                os.write(json.getBytes());
                runOnUiThread(() -> Toast.makeText(this, "Rules backed up successfully", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Backup failed", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void restoreRules(Uri uri) {
        executorService.execute(() -> {
            try (InputStream is = getContentResolver().openInputStream(uri);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<SmsFilterRule>>(){}.getType();
                List<SmsFilterRule> rules = gson.fromJson(reader, listType);

                if (rules != null) {
                    smsFilterRuleDao.deleteAllRules();
                    for (SmsFilterRule rule : rules) {
                        rule.setId(0); // Reset ID to allow auto-generation
                        smsFilterRuleDao.insertRule(rule);
                    }
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Rules restored successfully", Toast.LENGTH_SHORT).show();
                        loadRules();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Restore failed", Toast.LENGTH_SHORT).show());
            }
        });
    }
}