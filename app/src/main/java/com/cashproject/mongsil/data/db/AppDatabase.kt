package com.cashproject.mongsil.data.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.data.db.dao.CommentDao
import com.cashproject.mongsil.data.db.dao.LockerDao

@Database(entities = [SayingEntity::class, CommentEntity::class], version = 4)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun lockerDao(): LockerDao
    abstract fun commentDao(): CommentDao

    companion object {

        const val DB_FILE_NAME = "Mongsil.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, DB_FILE_NAME
            )
                .addMigrations(MIGRATION_2_3)
//                .addMigrations(MIGRATION_3_4)
                .build()
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Saying ADD COLUMN test TEXT")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("CREATE TABLE Saying_Backup (id TEXT, PRIMARY KEY (id))")
            execSQL("INSERT INTO Saying_Backup SELECT id FROM Saying")
            execSQL("DROP TABLE Saying")
            execSQL("ALTER TABLE Saying_Backup RENAME to Saying")
        }
    }
}