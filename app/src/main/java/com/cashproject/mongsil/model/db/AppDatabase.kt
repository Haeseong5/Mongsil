package com.cashproject.mongsil.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cashproject.mongsil.model.data.Saying
import com.google.firebase.firestore.auth.User

@Database(entities = [Saying::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lockerDao(): LockerDao

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