package com.cashproject.mongsil.kmp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cashproject.mongsil.kmp.database.dao.CounterDao
import com.cashproject.mongsil.kmp.database.dao.DiaryDao
import com.cashproject.mongsil.kmp.database.dao.EmoticonDao
import com.cashproject.mongsil.kmp.database.entity.CounterEntity
import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import com.cashproject.mongsil.kmp.database.entity.EmoticonEntity

@Database(
    entities = [DiaryEntity::class, EmoticonEntity::class, CounterEntity::class],
    version = 4,
    exportSchema = true,
)
abstract class MongsilRoomDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
    abstract fun emoticonDao(): EmoticonDao
    abstract fun counterDao(): CounterDao
}
