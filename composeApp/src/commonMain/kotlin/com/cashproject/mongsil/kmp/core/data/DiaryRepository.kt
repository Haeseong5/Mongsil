package com.cashproject.mongsil.kmp.core.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.cashproject.mongsil.kmp.database.DiaryEntity
import com.cashproject.mongsil.kmp.database.MongsilDatabase
import com.cashproject.mongsil.kmp.model.Emoticon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

/**
 * 일기 데이터를 관리하는 Repository
 * SQLDelight를 사용하여 로컬 데이터베이스에 저장/조회합니다.
 */
class DiaryRepository(private val database: MongsilDatabase) {
    
    /**
     * 특정 날짜의 일기를 Flow로 관찰합니다.
     */
    fun getDiaryByDateFlow(year: Int, month: Int, day: Int): Flow<DiaryEntity?> {
        return database.diaryQueries
            .getDiaryByDate(
                year = year.toLong(),
                month = month.toLong(),
                day = day.toLong()
            )
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
    }
    
    /**
     * 특정 날짜의 일기를 조회합니다.
     */
    suspend fun getDiaryByDate(year: Int, month: Int, day: Int): DiaryEntity? = 
        withContext(Dispatchers.Default) {
            database.diaryQueries
                .getDiaryByDate(
                    year = year.toLong(),
                    month = month.toLong(),
                    day = day.toLong()
                )
                .executeAsOneOrNull()
        }
    
    /**
     * 특정 년월의 모든 일기를 조회합니다.
     */
    suspend fun getDiariesByYearMonth(year: Int, month: Int): List<DiaryEntity> = 
        withContext(Dispatchers.Default) {
            database.diaryQueries
                .getDiariesByYearMonth(
                    year = year.toLong(),
                    month = month.toLong()
                )
                .executeAsList()
        }
    
    /**
     * 특정 년월의 모든 일기를 Flow로 관찰합니다.
     */
    fun getDiariesByYearMonthFlow(year: Int, month: Int): Flow<List<DiaryEntity>> {
        return database.diaryQueries
            .getDiariesByYearMonth(
                year = year.toLong(),
                month = month.toLong()
            )
            .asFlow()
            .mapToList(Dispatchers.Default)
    }
    
    /**
     * 모든 일기를 조회합니다.
     */
    suspend fun getAllDiaries(): List<DiaryEntity> = withContext(Dispatchers.Default) {
        database.diaryQueries.getAllDiaries().executeAsList()
    }
    
    /**
     * 일기를 저장하거나 업데이트합니다.
     */
    suspend fun saveDiary(
        year: Int,
        month: Int,
        day: Int,
        content: String,
        emoticonId: Long? = null,
        photoUri: String? = null
    ): Result<Unit> = withContext(Dispatchers.Default) {
        try {
            val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
            val existingDiary = getDiaryByDate(year, month, day)
            
            if (existingDiary != null) {
                // 업데이트
                database.diaryQueries.updateDiary(
                    content = content,
                    emoticonId = emoticonId,
                    photoUri = photoUri,
                    updatedAt = currentTimeMillis,
                    year = year.toLong(),
                    month = month.toLong(),
                    day = day.toLong()
                )
            } else {
                // 새로 저장
                database.diaryQueries.insertDiary(
                    year = year.toLong(),
                    month = month.toLong(),
                    day = day.toLong(),
                    content = content,
                    emoticonId = emoticonId,
                    photoUri = photoUri,
                    createdAt = currentTimeMillis,
                    updatedAt = currentTimeMillis
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 이모티콘 목록을 조회합니다.
     */
    suspend fun getEmoticons(): List<Emoticon> = withContext(Dispatchers.Default) {
        // TODO: 실제 이모티콘 데이터는 API나 데이터베이스에서 가져와야 합니다.
        // 지금은 임시 데이터를 반환합니다.
        listOf(
            Emoticon(1, "행복", "https://example.com/happy.png", "#333333", "#FFE5E5"),
            Emoticon(2, "슬픔", "https://example.com/sad.png", "#333333", "#E5F0FF"),
            Emoticon(3, "화남", "https://example.com/angry.png", "#333333", "#FFE5CC"),
            Emoticon(4, "평온", "https://example.com/calm.png", "#333333", "#E5FFE5")
        )
    }
    
    /**
     * 일기를 삭제합니다.
     */
    suspend fun deleteDiary(year: Int, month: Int, day: Int): Result<Unit> = 
        withContext(Dispatchers.Default) {
            try {
                database.diaryQueries.deleteDiary(
                    year = year.toLong(),
                    month = month.toLong(),
                    day = day.toLong()
                )
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    /**
     * 특정 날짜에 일기가 있는지 확인합니다.
     */
    suspend fun hasDiaryForDate(year: Int, month: Int, day: Int): Boolean = 
        withContext(Dispatchers.Default) {
            database.diaryQueries
                .hasDiaryForDate(
                    year = year.toLong(),
                    month = month.toLong(),
                    day = day.toLong()
                )
                .executeAsOne()
        }
}
