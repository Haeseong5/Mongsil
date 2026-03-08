package com.cashproject.mongsil.kmp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {

    @Query("SELECT * FROM DiaryEntity WHERE year = :year AND month = :month AND day = :day ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getDiaryByDate(year: Int, month: Int, day: Int): DiaryEntity?

    @Query("SELECT * FROM DiaryEntity WHERE year = :year AND month = :month AND day = :day ORDER BY updatedAt DESC LIMIT 1")
    fun getDiaryByDateFlow(year: Int, month: Int, day: Int): Flow<DiaryEntity?>

    @Query("SELECT * FROM DiaryEntity WHERE year = :year AND month = :month ORDER BY day ASC")
    suspend fun getDiariesByYearMonth(year: Int, month: Int): List<DiaryEntity>

    @Query("SELECT * FROM DiaryEntity WHERE year = :year AND month = :month ORDER BY day ASC")
    fun getDiariesByYearMonthFlow(year: Int, month: Int): Flow<List<DiaryEntity>>

    @Query("SELECT * FROM DiaryEntity ORDER BY year DESC, month DESC, day DESC")
    suspend fun getAllDiaries(): List<DiaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(diary: DiaryEntity)

    @Query("DELETE FROM DiaryEntity WHERE year = :year AND month = :month AND day = :day")
    suspend fun deleteDiary(year: Int, month: Int, day: Int)

    @Query("SELECT COUNT(*) FROM DiaryEntity WHERE year = :year AND month = :month AND day = :day")
    suspend fun countDiaryForDate(year: Int, month: Int, day: Int): Long
}
