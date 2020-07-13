package com.bytedance.todolist.database;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TodoListDao_Impl implements TodoListDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TodoListEntity> __insertionAdapterOfTodoListEntity;

  private final DateConverter __dateConverter = new DateConverter();

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteItemByContent;

  public TodoListDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTodoListEntity = new EntityInsertionAdapter<TodoListEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `todo` (`id`,`content`,`time`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TodoListEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        if (value.getContent() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getContent());
        }
        final long _tmp;
        _tmp = __dateConverter.toTimeStamp(value.getTime());
        stmt.bindLong(3, _tmp);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM todo";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteItemByContent = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM todo WHERE content=?";
        return _query;
      }
    };
  }

  @Override
  public long addTodo(final TodoListEntity entity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfTodoListEntity.insertAndReturnId(entity);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public void deleteItemByContent(final String content) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteItemByContent.acquire();
    int _argIndex = 1;
    if (content == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, content);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteItemByContent.release(_stmt);
    }
  }

  @Override
  public List<TodoListEntity> loadAll() {
    final String _sql = "SELECT * FROM todo";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfMTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
      final List<TodoListEntity> _result = new ArrayList<TodoListEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TodoListEntity _item;
        final String _tmpMContent;
        _tmpMContent = _cursor.getString(_cursorIndexOfMContent);
        final Date _tmpMTime;
        final long _tmp;
        _tmp = _cursor.getLong(_cursorIndexOfMTime);
        _tmpMTime = __dateConverter.fromTimeStamp(_tmp);
        _item = new TodoListEntity(_tmpMContent,_tmpMTime);
        final Long _tmpMId;
        if (_cursor.isNull(_cursorIndexOfMId)) {
          _tmpMId = null;
        } else {
          _tmpMId = _cursor.getLong(_cursorIndexOfMId);
        }
        _item.setId(_tmpMId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
