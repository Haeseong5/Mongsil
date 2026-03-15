package com.cashproject.mongsil.kmp.core.data

import com.cashproject.mongsil.kmp.core.data.datasource.DiaryLocalDataSource
import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import com.cashproject.mongsil.kmp.model.Emoticon
import kotlinx.coroutines.flow.Flow

class DiaryRepository(private val localDataSource: DiaryLocalDataSource) {

    fun getDiaryByDateFlow(year: Int, month: Int, day: Int): Flow<DiaryEntity?> =
        localDataSource.getDiaryByDateFlow(year, month, day)

    suspend fun getDiaryByDate(year: Int, month: Int, day: Int): DiaryEntity? =
        localDataSource.getDiaryByDate(year, month, day)

    suspend fun getDiariesByYearMonth(year: Int, month: Int): List<DiaryEntity> =
        localDataSource.getDiariesByYearMonth(year, month)

    fun getDiariesByYearMonthFlow(year: Int, month: Int): Flow<List<DiaryEntity>> =
        localDataSource.getDiariesByYearMonthFlow(year, month)

    suspend fun getAllDiaries(): List<DiaryEntity> =
        localDataSource.getAllDiaries()

    suspend fun getAllDiariesPaged(offset: Int, limit: Int, ascending: Boolean): List<DiaryEntity> =
        localDataSource.getAllDiariesPaged(offset, limit, ascending)

    suspend fun getAllDiariesCount(): Int =
        localDataSource.getAllDiariesCount()

    suspend fun saveDiary(
        year: Int,
        month: Int,
        day: Int,
        content: String,
        emoticonId: Long? = null,
        photoUri: String? = null,
        textAlign: String = "start",
        textColor: String = "FF000000",
        backgroundColor: String = "00000000",
    ): Result<Unit> = runCatching {
        localDataSource.saveDiary(year, month, day, content, emoticonId, photoUri, textAlign, textColor, backgroundColor)
    }

    suspend fun deleteDiary(year: Int, month: Int, day: Int): Result<Unit> = runCatching {
        localDataSource.deleteDiary(year, month, day)
    }

    suspend fun hasDiaryForDate(year: Int, month: Int, day: Int): Boolean =
        localDataSource.hasDiaryForDate(year, month, day)

    // TODO: 실제 이모티콘은 EmoticonRepository에서 가져와야 합니다.
    suspend fun getEmoticons(): List<Emoticon> = listOf(
        Emoticon(1, "행복", "https://example.com/happy.png", "#333333", "#FFE5E5"),
        Emoticon(2, "슬픔", "https://example.com/sad.png", "#333333", "#E5F0FF"),
        Emoticon(3, "화남", "https://example.com/angry.png", "#333333", "#FFE5CC"),
        Emoticon(4, "평온", "https://example.com/calm.png", "#333333", "#E5FFE5"),
    )
}
