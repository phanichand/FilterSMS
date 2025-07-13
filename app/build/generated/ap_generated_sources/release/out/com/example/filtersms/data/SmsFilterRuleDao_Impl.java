package com.example.filtersms.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SmsFilterRuleDao_Impl implements SmsFilterRuleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SmsFilterRule> __insertionAdapterOfSmsFilterRule;

  private final EntityDeletionOrUpdateAdapter<SmsFilterRule> __deletionAdapterOfSmsFilterRule;

  private final EntityDeletionOrUpdateAdapter<SmsFilterRule> __updateAdapterOfSmsFilterRule;

  public SmsFilterRuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSmsFilterRule = new EntityInsertionAdapter<SmsFilterRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sms_filter_rules` (`id`,`sender`,`messagePattern`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final SmsFilterRule entity) {
        statement.bindLong(1, entity.id);
        if (entity.sender == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.sender);
        }
        if (entity.messagePattern == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.messagePattern);
        }
      }
    };
    this.__deletionAdapterOfSmsFilterRule = new EntityDeletionOrUpdateAdapter<SmsFilterRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `sms_filter_rules` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final SmsFilterRule entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfSmsFilterRule = new EntityDeletionOrUpdateAdapter<SmsFilterRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sms_filter_rules` SET `id` = ?,`sender` = ?,`messagePattern` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final SmsFilterRule entity) {
        statement.bindLong(1, entity.id);
        if (entity.sender == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.sender);
        }
        if (entity.messagePattern == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.messagePattern);
        }
        statement.bindLong(4, entity.id);
      }
    };
  }

  @Override
  public void insertRule(final SmsFilterRule rule) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSmsFilterRule.insert(rule);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRule(final SmsFilterRule rule) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSmsFilterRule.handle(rule);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateRule(final SmsFilterRule rule) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSmsFilterRule.handle(rule);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<SmsFilterRule> getAllRules() {
    final String _sql = "SELECT * FROM sms_filter_rules ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSender = CursorUtil.getColumnIndexOrThrow(_cursor, "sender");
      final int _cursorIndexOfMessagePattern = CursorUtil.getColumnIndexOrThrow(_cursor, "messagePattern");
      final List<SmsFilterRule> _result = new ArrayList<SmsFilterRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SmsFilterRule _item;
        final String _tmpSender;
        if (_cursor.isNull(_cursorIndexOfSender)) {
          _tmpSender = null;
        } else {
          _tmpSender = _cursor.getString(_cursorIndexOfSender);
        }
        final String _tmpMessagePattern;
        if (_cursor.isNull(_cursorIndexOfMessagePattern)) {
          _tmpMessagePattern = null;
        } else {
          _tmpMessagePattern = _cursor.getString(_cursorIndexOfMessagePattern);
        }
        _item = new SmsFilterRule(_tmpSender,_tmpMessagePattern);
        _item.id = _cursor.getInt(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public SmsFilterRule getRuleById(final int ruleId) {
    final String _sql = "SELECT * FROM sms_filter_rules WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, ruleId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSender = CursorUtil.getColumnIndexOrThrow(_cursor, "sender");
      final int _cursorIndexOfMessagePattern = CursorUtil.getColumnIndexOrThrow(_cursor, "messagePattern");
      final SmsFilterRule _result;
      if (_cursor.moveToFirst()) {
        final String _tmpSender;
        if (_cursor.isNull(_cursorIndexOfSender)) {
          _tmpSender = null;
        } else {
          _tmpSender = _cursor.getString(_cursorIndexOfSender);
        }
        final String _tmpMessagePattern;
        if (_cursor.isNull(_cursorIndexOfMessagePattern)) {
          _tmpMessagePattern = null;
        } else {
          _tmpMessagePattern = _cursor.getString(_cursorIndexOfMessagePattern);
        }
        _result = new SmsFilterRule(_tmpSender,_tmpMessagePattern);
        _result.id = _cursor.getInt(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
