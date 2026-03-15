package com.cashproject.mongsil.kmp.core.data.datasource

import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow

interface DiaryLocalDataSource {
    fun getDiaryByDateFlow(year: Int, month: Int, day: Int): Flow<DiaryEntity?>
    suspend fun getDiaryByDate(year: Int, month: Int, day: Int): DiaryEntity?
    suspend fun getDiariesByYearMonth(year: Int, month: Int): List<DiaryEntity>
    fun getDiariesByYearMonthFlow(year: Int, month: Int): Flow<List<DiaryEntity>>
    suspend fun getAllDiaries(): List<DiaryEntity>
    suspend fun getAllDiariesPaged(offset: Int, limit: Int, ascending: Boolean): List<DiaryEntity>
    suspend fun getAllDiariesCount(): Int
    suspend fun saveDiary(
        year: Int,
        month: Int,
        day: Int,
        content: String,
        emoticonId: Long?,
        photoUri: String?,
        textAlign: String,
        textColor: String,
        backgroundColor: String,
    )
    suspend fun deleteDiary(year: Int, month: Int, day: Int)
    suspend fun hasDiaryForDate(year: Int, month: Int, day: Int): Boolean
}
