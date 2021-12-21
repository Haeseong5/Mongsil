package com.cashproject.mongsil.model.db

import android.content.Context
import androidx.room.*
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

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "Mongsil.db"
            )
//                .fallbackToDestructiveMigration() //기존 데이터 손실을 허용하고 마이그레션
                .build()
    }
}