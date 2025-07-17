package com.example.filtersms.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SmsFilterRule.class, LogEntry.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SmsFilterRuleDao smsFilterRuleDao();
    public abstract LogEntryDao logEntryDao();
}