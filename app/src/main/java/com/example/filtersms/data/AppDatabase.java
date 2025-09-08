package com.example.filtersms.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {SmsFilterRule.class, LogEntry.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SmsFilterRuleDao smsFilterRuleDao();
    public abstract LogEntryDao logEntryDao();

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE sms_filter_rules ADD COLUMN sendToGoogleSheet INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE sms_filter_rules ADD COLUMN sheetId TEXT");
            database.execSQL("ALTER TABLE sms_filter_rules ADD COLUMN sheetName TEXT");
        }
    };

    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE sms_filter_rules ADD COLUMN sendToWebhook INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE sms_filter_rules ADD COLUMN webhookUrl TEXT");
        }
    };
}