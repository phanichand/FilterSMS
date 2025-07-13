package com.example.filtersms.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SmsFilterRule.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SmsFilterRuleDao smsFilterRuleDao();
}