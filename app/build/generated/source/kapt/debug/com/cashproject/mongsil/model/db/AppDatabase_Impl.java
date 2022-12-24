package com.cashproject.mongsil.model.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.cashproject.mongsil.data.db.AppDatabase;
import com.cashproject.mongsil.data.db.dao.CommentDao;
import com.cashproject.mongsil.model.db.dao.CommentDao_Impl;
import com.cashproject.mongsil.data.db.dao.LockerDao;
import com.cashproject.mongsil.model.db.dao.LockerDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile LockerDao _lockerDao;

  private volatile CommentDao _commentDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Saying` (`docId` TEXT NOT NULL, `image` TEXT NOT NULL, `s` TEXT NOT NULL, `date` INTEGER NOT NULL, `test` TEXT NOT NULL, PRIMARY KEY(`docId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Comment` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT NOT NULL, `emotion` INTEGER NOT NULL, `time` INTEGER NOT NULL, `date` INTEGER NOT NULL, `docId` TEXT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3946494082544ae7fd36efde164173f8')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Saying`");
        _db.execSQL("DROP TABLE IF EXISTS `Comment`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsSaying = new HashMap<String, TableInfo.Column>(5);
        _columnsSaying.put("docId", new TableInfo.Column("docId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaying.put("image", new TableInfo.Column("image", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaying.put("s", new TableInfo.Column("s", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaying.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaying.put("test", new TableInfo.Column("test", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSaying = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSaying = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSaying = new TableInfo("Saying", _columnsSaying, _foreignKeysSaying, _indicesSaying);
        final TableInfo _existingSaying = TableInfo.read(_db, "Saying");
        if (! _infoSaying.equals(_existingSaying)) {
          return new RoomOpenHelper.ValidationResult(false, "Saying(com.cashproject.mongsil.data.entity.Saying).\n"
                  + " Expected:\n" + _infoSaying + "\n"
                  + " Found:\n" + _existingSaying);
        }
        final HashMap<String, TableInfo.Column> _columnsComment = new HashMap<String, TableInfo.Column>(6);
        _columnsComment.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComment.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComment.put("emotion", new TableInfo.Column("emotion", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComment.put("time", new TableInfo.Column("time", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComment.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComment.put("docId", new TableInfo.Column("docId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysComment = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesComment = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoComment = new TableInfo("Comment", _columnsComment, _foreignKeysComment, _indicesComment);
        final TableInfo _existingComment = TableInfo.read(_db, "Comment");
        if (! _infoComment.equals(_existingComment)) {
          return new RoomOpenHelper.ValidationResult(false, "Comment(com.cashproject.mongsil.data.entity.Comment).\n"
                  + " Expected:\n" + _infoComment + "\n"
                  + " Found:\n" + _existingComment);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3946494082544ae7fd36efde164173f8", "7a1177d48ce0fcac3edd337f5921b308");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Saying","Comment");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Saying`");
      _db.execSQL("DELETE FROM `Comment`");
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
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(LockerDao.class, LockerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CommentDao.class, CommentDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public LockerDao lockerDao() {
    if (_lockerDao != null) {
      return _lockerDao;
    } else {
      synchronized(this) {
        if(_lockerDao == null) {
          _lockerDao = new LockerDao_Impl(this);
        }
        return _lockerDao;
      }
    }
  }

  @Override
  public CommentDao commentDao() {
    if (_commentDao != null) {
      return _commentDao;
    } else {
      synchronized(this) {
        if(_commentDao == null) {
          _commentDao = new CommentDao_Impl(this);
        }
        return _commentDao;
      }
    }
  }
}
