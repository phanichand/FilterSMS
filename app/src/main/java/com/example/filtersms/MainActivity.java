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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.filtersms.data.AppDatabase;
import com.example.filtersms.data.SmsFilterRule;
import com.example.filtersms.data.SmsFilterRuleDao;
import com.example.filtersms.ui.RuleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    private RecyclerView recyclerViewRules;
    private RuleAdapter ruleAdapter;
    private FloatingActionButton fabAddRule;
    private Button buttonEmailSettings;

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

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "sms-filter-db").build();
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
        if (item.getItemId() == R.id.action_logs) {
            Intent intent = new Intent(this, LogsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRules();
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
                Toast.makeText(this, "SMS permissions are required to filter messages.", Toast.LENGTH_LONG).show();
            }
        }
    }
}