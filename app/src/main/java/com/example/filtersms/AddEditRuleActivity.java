package com.example.filtersms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filtersms.data.AppDatabase;
import com.example.filtersms.data.SmsFilterRule;
import com.example.filtersms.data.SmsFilterRuleDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditRuleActivity extends AppCompatActivity {

    private EditText editTextSender;
    private EditText editTextMessagePattern;
    private Button buttonSaveRule;

    private AppDatabase db;
    private SmsFilterRuleDao smsFilterRuleDao;
    private ExecutorService executorService;

    private int ruleId = -1; // -1 indicates a new rule, otherwise it's an existing rule's ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_rule);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add/Edit Rule");
        }

        editTextSender = findViewById(R.id.editTextSender);
        editTextMessagePattern = findViewById(R.id.editTextMessagePattern);
        buttonSaveRule = findViewById(R.id.buttonSaveRule);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "sms-filter-db").fallbackToDestructiveMigration().build();
        smsFilterRuleDao = db.smsFilterRuleDao();
        executorService = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.EXTRA_RULE_ID)) {
            ruleId = intent.getIntExtra(MainActivity.EXTRA_RULE_ID, -1);
            loadRule(ruleId);
        }

        buttonSaveRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRule();
            }
        });
    }

    private void loadRule(final int id) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final SmsFilterRule rule = smsFilterRuleDao.getRuleById(id);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rule != null) {
                            editTextSender.setText(rule.getSender());
                            editTextMessagePattern.setText(rule.getMessagePattern());
                        }
                    }
                });
            }
        });
    }

    private void saveRule() {
        final String sender = editTextSender.getText().toString().trim();
        final String messagePattern = editTextMessagePattern.getText().toString().trim();

        

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (ruleId == -1) {
                    // New rule
                    smsFilterRuleDao.insertRule(new SmsFilterRule(sender, messagePattern));
                } else {
                    // Update existing rule
                    SmsFilterRule rule = smsFilterRuleDao.getRuleById(ruleId);
                    if (rule != null) {
                        rule.sender = sender;
                        rule.messagePattern = messagePattern;
                        smsFilterRuleDao.updateRule(rule);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddEditRuleActivity.this, "Rule saved!", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to MainActivity
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}