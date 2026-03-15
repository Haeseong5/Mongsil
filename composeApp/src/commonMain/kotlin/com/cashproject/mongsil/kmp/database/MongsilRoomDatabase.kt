package com.cashproject.mongsil.kmp.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.cashproject.mongsil.kmp.database.dao.CounterDao
import com.cashproject.mongsil.kmp.database.dao.DiaryDao
import com.cashproject.mongsil.kmp.database.dao.EmoticonDao
import com.cashproject.mongsil.kmp.database.entity.CounterEntity
import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import com.cashproject.mongsil.kmp.database.entity.EmoticonEntity

@Database(
    entities = [DiaryEntity::class, EmoticonEntity::class, CounterEntity::class],
    version = 7,
    exportSchema = true,
)
@ConstructedBy(MongsilRoomDatabaseConstructor::class)
abstract class MongsilRoomDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
    abstract fun emoticonDao(): EmoticonDao
    abstract fun counterDao(): CounterDao
}

// KMP requires an expect object for Room's code generation on non-Android targets
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MongsilRoomDatabaseConstructor : RoomDatabaseConstructor<MongsilRoomDatabase> {
    override fun initialize(): MongsilRoomDatabase
}
