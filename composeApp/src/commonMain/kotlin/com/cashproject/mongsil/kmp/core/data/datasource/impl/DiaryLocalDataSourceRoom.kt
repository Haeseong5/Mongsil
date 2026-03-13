package com.cashproject.mongsil.kmp.core.data.datasource.impl

import com.cashproject.mongsil.kmp.core.data.datasource.DiaryLocalDataSource
import com.cashproject.mongsil.kmp.database.dao.DiaryDao
import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

class DiaryLocalDataSourceRoom(private val diaryDao: DiaryDao) : DiaryLocalDataSource {

    override fun getDiaryByDateFlow(year: Int, month: Int, day: Int): Flow<DiaryEntity?> =
        diaryDao.getDiaryByDateFlow(year, month, day)

    override suspend fun getDiaryByDate(year: Int, month: Int, day: Int): DiaryEntity? =
        diaryDao.getDiaryByDate(year, month, day)

    override suspend fun getDiariesByYearMonth(year: Int, month: Int): List<DiaryEntity> =
        diaryDao.getDiariesByYearMonth(year, month)

    override fun getDiariesByYearMonthFlow(year: Int, month: Int): Flow<List<DiaryEntity>> =
        diaryDao.getDiariesByYearMonthFlow(year, month)

    override suspend fun getAllDiaries(): List<DiaryEntity> =
        diaryDao.getAllDiaries()

    override suspend fun getAllDiariesPaged(
        offset: Int,
        limit: Int,
        ascending: Boolean
    ): List<DiaryEntity> =
        if (ascending) diaryDao.getAllDiariesPagedAsc(limit, offset)
        else diaryDao.getAllDiariesPagedDesc(limit, offset)

    override suspend fun getAllDiariesCount(): Int =
        diaryDao.getAllDiariesCount()

    @OptIn(ExperimentalTime::class)
    override suspend fun saveDiary(
        year: Int,
        month: Int,
        day: Int,
        content: String,
        emoticonId: Long?,
        photoUri: String?,
        textAlign: String,
        textColor: String,
    ) {
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val existing = diaryDao.getDiaryByDate(year, month, day)
        diaryDao.insertOrUpdate(
            DiaryEntity(
                id = existing?.id ?: 0,
                year = year,
                month = month,
                day = day,
                content = content,
                emoticonId = emoticonId,
                photoUri = photoUri,
                textAlign = textAlign,
                textColor = textColor,
                createdAt = existing?.createdAt ?: now,
                updatedAt = now,
            )
        )
    }

    override suspend fun deleteDiary(year: Int, month: Int, day: Int) {
        diaryDao.deleteDiary(year, month, day)
    }

    override suspend fun hasDiaryForDate(year: Int, month: Int, day: Int): Boolean =
        diaryDao.countDiaryForDate(year, month, day) > 0
}
