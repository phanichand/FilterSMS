package com.example.filtersms.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile SmsFilterRuleDao _smsFilterRuleDao;

  private volatile LogEntryDao _logEntryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `sms_filter_rules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sender` TEXT, `messagePattern` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `log_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` TEXT, `message` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8767381f346074671eed36069164ed3c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `sms_filter_rules`");
        db.execSQL("DROP TABLE IF EXISTS `log_table`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSmsFilterRules = new HashMap<String, TableInfo.Column>(3);
        _columnsSmsFilterRules.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsFilterRules.put("sender", new TableInfo.Column("sender", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsFilterRules.put("messagePattern", new TableInfo.Column("messagePattern", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSmsFilterRules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSmsFilterRules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSmsFilterRules = new TableInfo("sms_filter_rules", _columnsSmsFilterRules, _foreignKeysSmsFilterRules, _indicesSmsFilterRules);
        final TableInfo _existingSmsFilterRules = TableInfo.read(db, "sms_filter_rules");
        if (!_infoSmsFilterRules.equals(_existingSmsFilterRules)) {
          return new RoomOpenHelper.ValidationResult(false, "sms_filter_rules(com.example.filtersms.data.SmsFilterRule).\n"
                  + " Expected:\n" + _infoSmsFilterRules + "\n"
                  + " Found:\n" + _existingSmsFilterRules);
        }
        final HashMap<String, TableInfo.Column> _columnsLogTable = new HashMap<String, TableInfo.Column>(3);
        _columnsLogTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLogTable.put("timestamp", new TableInfo.Column("timestamp", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLogTable.put("message", new TableInfo.Column("message", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLogTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLogTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLogTable = new TableInfo("log_table", _columnsLogTable, _foreignKeysLogTable, _indicesLogTable);
        final TableInfo _existingLogTable = TableInfo.read(db, "log_table");
        if (!_infoLogTable.equals(_existingLogTable)) {
          return new RoomOpenHelper.ValidationResult(false, "log_table(com.example.filtersms.data.LogEntry).\n"
                  + " Expected:\n" + _infoLogTable + "\n"
                  + " Found:\n" + _existingLogTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "8767381f346074671eed36069164ed3c", "1c368b37790d415ee617b4b6d134751c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "sms_filter_rules","log_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `sms_filter_rules`");
      _db.execSQL("DELETE FROM `log_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SmsFilterRuleDao.class, SmsFilterRuleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LogEntryDao.class, LogEntryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SmsFilterRuleDao smsFilterRuleDao() {
    if (_smsFilterRuleDao != null) {
      return _smsFilterRuleDao;
    } else {
      synchronized(this) {
        if(_smsFilterRuleDao == null) {
          _smsFilterRuleDao = new SmsFilterRuleDao_Impl(this);
        }
        return _smsFilterRuleDao;
      }
    }
  }

  @Override
  public LogEntryDao logEntryDao() {
    if (_logEntryDao != null) {
      return _logEntryDao;
    } else {
      synchronized(this) {
        if(_logEntryDao == null) {
          _logEntryDao = new LogEntryDao_Impl(this);
        }
        return _logEntryDao;
      }
    }
  }
}
