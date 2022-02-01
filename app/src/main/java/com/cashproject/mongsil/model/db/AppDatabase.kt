package com.cashproject.mongsil.model.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.dao.CommentDao
import com.cashproject.mongsil.model.db.dao.LockerDao

//https://github.com/android/architecture-components-samples/tree/master/PersistenceMigrationsSample/app/src/room3/java/com/example/android/persistence/migrations

@Database(entities = [Saying::class, Comment::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lockerDao(): LockerDao
    abstract fun commentDao(): CommentDao

    companion object {

        private const val DB_FILE_NAME = "Mongsil.db"

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
            ).build()
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
    }
}