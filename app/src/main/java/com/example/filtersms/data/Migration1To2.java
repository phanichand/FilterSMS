package com.example.filtersms.data;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration1To2 extends Migration {
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `log_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` TEXT, `message` TEXT)");
        }
    };

    public Migration1To2() {
        super(1, 2);
    }

    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // This empty migration is here because the actual migration logic is in MIGRATION_1_2 static field.
        // This constructor and method are kept for compatibility with how Migration classes are typically defined,
        // but the Room database builder will use the static MIGRATION_1_2 field directly.
    }
}