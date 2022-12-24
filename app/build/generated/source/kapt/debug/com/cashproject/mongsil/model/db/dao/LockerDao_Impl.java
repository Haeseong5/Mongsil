package com.cashproject.mongsil.model.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.cashproject.mongsil.data.db.dao.LockerDao;
import com.cashproject.mongsil.data.db.entity.Saying;
import com.cashproject.mongsil.data.db.DateConverter;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class LockerDao_Impl implements LockerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Saying> __insertionAdapterOfSaying;

  private final DateConverter __dateConverter = new DateConverter();

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public LockerDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSaying = new EntityInsertionAdapter<Saying>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Saying` (`docId`,`image`,`s`,`date`,`test`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Saying value) {
        if (value.getDocId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getDocId());
        }
        if (value.getImage() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImage());
        }
        if (value.getSquareImage() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSquareImage());
        }
        final Long _tmp = __dateConverter.dateToTimestamp(value.getDate());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.getTest() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTest());
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM Saying WHERE docId = ?";
        return _query;
      }
    };
  }

  @Override
  public Completable insert(final Saying saying) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSaying.insert(saying);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable delete(final String docId) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        if (docId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, docId);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    });
  }

  @Override
  public Object getAll(final Continuation<? super List<Saying>> continuation) {
    final String _sql = "SELECT * FROM Saying";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Saying>>() {
      @Override
      public List<Saying> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDocId = CursorUtil.getColumnIndexOrThrow(_cursor, "docId");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfSquareImage = CursorUtil.getColumnIndexOrThrow(_cursor, "s");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTest = CursorUtil.getColumnIndexOrThrow(_cursor, "test");
          final List<Saying> _result = new ArrayList<Saying>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Saying _item;
            final String _tmpDocId;
            if (_cursor.isNull(_cursorIndexOfDocId)) {
              _tmpDocId = null;
            } else {
              _tmpDocId = _cursor.getString(_cursorIndexOfDocId);
            }
            final String _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getString(_cursorIndexOfImage);
            }
            final String _tmpSquareImage;
            if (_cursor.isNull(_cursorIndexOfSquareImage)) {
              _tmpSquareImage = null;
            } else {
              _tmpSquareImage = _cursor.getString(_cursorIndexOfSquareImage);
            }
            final Date _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            _tmpDate = __dateConverter.fromTimestamp(_tmp);
            final String _tmpTest;
            if (_cursor.isNull(_cursorIndexOfTest)) {
              _tmpTest = null;
            } else {
              _tmpTest = _cursor.getString(_cursorIndexOfTest);
            }
            _item = new Saying(_tmpDocId,_tmpImage,_tmpSquareImage,_tmpDate,_tmpTest);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Maybe<Saying> findByDocId(final String docId) {
    final String _sql = "SELECT * FROM Saying WHERE docId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (docId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, docId);
    }
    return Maybe.fromCallable(new Callable<Saying>() {
      @Override
      public Saying call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDocId = CursorUtil.getColumnIndexOrThrow(_cursor, "docId");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfSquareImage = CursorUtil.getColumnIndexOrThrow(_cursor, "s");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTest = CursorUtil.getColumnIndexOrThrow(_cursor, "test");
          final Saying _result;
          if(_cursor.moveToFirst()) {
            final String _tmpDocId;
            if (_cursor.isNull(_cursorIndexOfDocId)) {
              _tmpDocId = null;
            } else {
              _tmpDocId = _cursor.getString(_cursorIndexOfDocId);
            }
            final String _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getString(_cursorIndexOfImage);
            }
            final String _tmpSquareImage;
            if (_cursor.isNull(_cursorIndexOfSquareImage)) {
              _tmpSquareImage = null;
            } else {
              _tmpSquareImage = _cursor.getString(_cursorIndexOfSquareImage);
            }
            final Date _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            _tmpDate = __dateConverter.fromTimestamp(_tmp);
            final String _tmpTest;
            if (_cursor.isNull(_cursorIndexOfTest)) {
              _tmpTest = null;
            } else {
              _tmpTest = _cursor.getString(_cursorIndexOfTest);
            }
            _result = new Saying(_tmpDocId,_tmpImage,_tmpSquareImage,_tmpDate,_tmpTest);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
