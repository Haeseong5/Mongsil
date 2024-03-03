package com.cashproject.mongsil.database

import android.content.Context
import androidx.room.*
import com.cashproject.mongsil.common.App
import com.cashproject.mongsil.database.dao.BookmarkDao
import com.cashproject.mongsil.database.dao.DiaryDao
import com.cashproject.mongsil.database.model.CommentEntity
import com.cashproject.mongsil.database.model.SayingEntity
import com.cashproject.mongsil.database.utils.DateConverter

@Database(entities = [SayingEntity::class, CommentEntity::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao
    abstract fun diaryDao(): DiaryDao

    companion object {

        const val DB_FILE_NAME = "Mongsil.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context = App.context): AppDatabase =
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

//val MIGRATION_2_3 = object : Migration(2, 3) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("ALTER TABLE Saying ADD COLUMN test TEXT")
//    }
//}