package com.example.filtersms.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
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

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllRules;

  public SmsFilterRuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSmsFilterRule = new EntityInsertionAdapter<SmsFilterRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sms_filter_rules` (`id`,`sender`,`messagePattern`,`sendToGoogleSheet`,`sheetId`,`sheetName`,`sendToWebhook`,`webhookUrl`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
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
        final int _tmp = entity.sendToGoogleSheet ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.sheetId == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.sheetId);
        }
        if (entity.sheetName == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.sheetName);
        }
        final int _tmp_1 = entity.sendToWebhook ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.webhookUrl == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.webhookUrl);
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
        return "UPDATE OR ABORT `sms_filter_rules` SET `id` = ?,`sender` = ?,`messagePattern` = ?,`sendToGoogleSheet` = ?,`sheetId` = ?,`sheetName` = ?,`sendToWebhook` = ?,`webhookUrl` = ? WHERE `id` = ?";
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
        final int _tmp = entity.sendToGoogleSheet ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.sheetId == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.sheetId);
        }
        if (entity.sheetName == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.sheetName);
        }
        final int _tmp_1 = entity.sendToWebhook ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.webhookUrl == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.webhookUrl);
        }
        statement.bindLong(9, entity.id);
      }
    };
    this.__preparedStmtOfDeleteAllRules = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sms_filter_rules";
        return _query;
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
  public void deleteAllRules() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllRules.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllRules.release(_stmt);
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
      final int _cursorIndexOfSendToGoogleSheet = CursorUtil.getColumnIndexOrThrow(_cursor, "sendToGoogleSheet");
      final int _cursorIndexOfSheetId = CursorUtil.getColumnIndexOrThrow(_cursor, "sheetId");
      final int _cursorIndexOfSheetName = CursorUtil.getColumnIndexOrThrow(_cursor, "sheetName");
      final int _cursorIndexOfSendToWebhook = CursorUtil.getColumnIndexOrThrow(_cursor, "sendToWebhook");
      final int _cursorIndexOfWebhookUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "webhookUrl");
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
        final boolean _tmpSendToGoogleSheet;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSendToGoogleSheet);
        _tmpSendToGoogleSheet = _tmp != 0;
        final String _tmpSheetId;
        if (_cursor.isNull(_cursorIndexOfSheetId)) {
          _tmpSheetId = null;
        } else {
          _tmpSheetId = _cursor.getString(_cursorIndexOfSheetId);
        }
        final String _tmpSheetName;
        if (_cursor.isNull(_cursorIndexOfSheetName)) {
          _tmpSheetName = null;
        } else {
          _tmpSheetName = _cursor.getString(_cursorIndexOfSheetName);
        }
        final boolean _tmpSendToWebhook;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfSendToWebhook);
        _tmpSendToWebhook = _tmp_1 != 0;
        final String _tmpWebhookUrl;
        if (_cursor.isNull(_cursorIndexOfWebhookUrl)) {
          _tmpWebhookUrl = null;
        } else {
          _tmpWebhookUrl = _cursor.getString(_cursorIndexOfWebhookUrl);
        }
        _item = new SmsFilterRule(_tmpSender,_tmpMessagePattern,_tmpSendToGoogleSheet,_tmpSheetId,_tmpSheetName,_tmpSendToWebhook,_tmpWebhookUrl);
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
      final int _cursorIndexOfSendToGoogleSheet = CursorUtil.getColumnIndexOrThrow(_cursor, "sendToGoogleSheet");
      final int _cursorIndexOfSheetId = CursorUtil.getColumnIndexOrThrow(_cursor, "sheetId");
      final int _cursorIndexOfSheetName = CursorUtil.getColumnIndexOrThrow(_cursor, "sheetName");
      final int _cursorIndexOfSendToWebhook = CursorUtil.getColumnIndexOrThrow(_cursor, "sendToWebhook");
      final int _cursorIndexOfWebhookUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "webhookUrl");
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
        final boolean _tmpSendToGoogleSheet;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSendToGoogleSheet);
        _tmpSendToGoogleSheet = _tmp != 0;
        final String _tmpSheetId;
        if (_cursor.isNull(_cursorIndexOfSheetId)) {
          _tmpSheetId = null;
        } else {
          _tmpSheetId = _cursor.getString(_cursorIndexOfSheetId);
        }
        final String _tmpSheetName;
        if (_cursor.isNull(_cursorIndexOfSheetName)) {
          _tmpSheetName = null;
        } else {
          _tmpSheetName = _cursor.getString(_cursorIndexOfSheetName);
        }
        final boolean _tmpSendToWebhook;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfSendToWebhook);
        _tmpSendToWebhook = _tmp_1 != 0;
        final String _tmpWebhookUrl;
        if (_cursor.isNull(_cursorIndexOfWebhookUrl)) {
          _tmpWebhookUrl = null;
        } else {
          _tmpWebhookUrl = _cursor.getString(_cursorIndexOfWebhookUrl);
        }
        _result = new SmsFilterRule(_tmpSender,_tmpMessagePattern,_tmpSendToGoogleSheet,_tmpSheetId,_tmpSheetName,_tmpSendToWebhook,_tmpWebhookUrl);
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
