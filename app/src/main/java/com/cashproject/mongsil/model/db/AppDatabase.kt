package com.cashproject.mongsil.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.db.dao.CommentDao
import com.cashproject.mongsil.model.db.dao.LockerDao

@Database(entities = arrayOf(LikeSaying::class, Comment::class), version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lockerDao(): LockerDao
    abstract fun commentDao(): CommentDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "Mongsil.db")
                .build()
    }
}