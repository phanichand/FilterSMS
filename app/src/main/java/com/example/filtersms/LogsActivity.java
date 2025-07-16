package com.example.filtersms;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.filtersms.data.AppDatabase;
import com.example.filtersms.data.LogEntry;
import com.example.filtersms.data.LogEntryDao;
import com.example.filtersms.ui.LogAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLogs;
    private LogAdapter logAdapter;

    private AppDatabase db;
    private LogEntryDao logEntryDao;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("App Logs");
        }

        recyclerViewLogs = findViewById(R.id.recyclerViewLogs);
        recyclerViewLogs.setLayoutManager(new LinearLayoutManager(this));
        logAdapter = new LogAdapter();
        recyclerViewLogs.setAdapter(logAdapter);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "sms-filter-db").fallbackToDestructiveMigration().build();
        logEntryDao = db.logEntryDao();
        executorService = Executors.newSingleThreadExecutor();

        loadLogs();
    }

    private void loadLogs() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<LogEntry> logs = logEntryDao.getAllLogs();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logAdapter.setLogs(logs);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_clear_logs) {
            clearLogs();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearLogs() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                logEntryDao.deleteAllLogs();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadLogs();
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
}