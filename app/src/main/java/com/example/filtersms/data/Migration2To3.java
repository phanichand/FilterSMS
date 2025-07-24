package com.example.filtersms.data;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration2To3 extends Migration {
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since the LogEntry table was added in version 2, and we are just incrementing the version
            // due to previous schema changes, no actual schema change is needed here for LogEntry.
            // However, if there were changes to SmsFilterRule table in this version, they would go here.
            // For now, this migration is empty as the schema for existing tables is unchanged from v2 to v3.
        }
    };

    public Migration2To3() {
        super(2, 3);
    }

    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // This empty migration is here because the actual migration logic is in MIGRATION_2_3 static field.
        // This constructor and method are kept for compatibility with how Migration classes are typically defined,
        // but the Room database builder will use the static MIGRATION_2_3 field directly.
    }
}