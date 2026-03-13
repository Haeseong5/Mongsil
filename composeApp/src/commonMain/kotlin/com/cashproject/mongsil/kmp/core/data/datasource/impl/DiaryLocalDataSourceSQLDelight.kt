package com.cashproject.mongsil.kmp.core.data.datasource.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.cashproject.mongsil.kmp.core.data.datasource.DiaryLocalDataSource
import com.cashproject.mongsil.kmp.database.MongsilDatabase
import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime
import com.cashproject.mongsil.kmp.database.DiaryEntity as SQLDelightDiaryEntity

class DiaryLocalDataSourceSQLDelight(private val database: MongsilDatabase) : DiaryLocalDataSource {

    override fun getDiaryByDateFlow(year: Int, month: Int, day: Int): Flow<DiaryEntity?> =
        database.diaryQueries
            .getDiaryByDate(year.toLong(), month.toLong(), day.toLong())
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toCommon() }

    override suspend fun getDiaryByDate(year: Int, month: Int, day: Int): DiaryEntity? =
        withContext(Dispatchers.Default) {
            database.diaryQueries
                .getDiaryByDate(year.toLong(), month.toLong(), day.toLong())
                .executeAsOneOrNull()
                ?.toCommon()
        }

    override suspend fun getDiariesByYearMonth(year: Int, month: Int): List<DiaryEntity> =
        withContext(Dispatchers.Default) {
            database.diaryQueries
                .getDiariesByYearMonth(year.toLong(), month.toLong())
                .executeAsList()
                .map { it.toCommon() }
        }

    override fun getDiariesByYearMonthFlow(year: Int, month: Int): Flow<List<DiaryEntity>> =
        database.diaryQueries
            .getDiariesByYearMonth(year.toLong(), month.toLong())
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toCommon() } }

    override suspend fun getAllDiaries(): List<DiaryEntity> =
        withContext(Dispatchers.Default) {
            database.diaryQueries.getAllDiaries().executeAsList().map { it.toCommon() }
        }

    override suspend fun getAllDiariesPaged(
        offset: Int,
        limit: Int,
        ascending: Boolean
    ): List<DiaryEntity> =
        withContext(Dispatchers.Default) {
            val query = if (ascending) {
                database.diaryQueries.getAllDiariesPagedAsc(limit.toLong(), offset.toLong())
            } else {
                database.diaryQueries.getAllDiariesPagedDesc(limit.toLong(), offset.toLong())
            }
            query.executeAsList().map { it.toCommon() }
        }

    override suspend fun getAllDiariesCount(): Int =
        withContext(Dispatchers.Default) {
            database.diaryQueries.getAllDiariesCount().executeAsOne().toInt()
        }

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
    ) = withContext(Dispatchers.Default) {
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val existing = getDiaryByDate(year, month, day)
        if (existing != null) {
            database.diaryQueries.updateDiary(
                content = content,
                emoticonId = emoticonId,
                photoUri = photoUri,
                textAlign = textAlign,
                textColor = textColor,
                updatedAt = now,
                year = year.toLong(),
                month = month.toLong(),
                day = day.toLong(),
            )
        } else {
            database.diaryQueries.insertDiary(
                year = year.toLong(),
                month = month.toLong(),
                day = day.toLong(),
                content = content,
                emoticonId = emoticonId,
                photoUri = photoUri,
                textAlign = textAlign,
                textColor = textColor,
                createdAt = now,
                updatedAt = now,
            )
        }
    }

    override suspend fun deleteDiary(year: Int, month: Int, day: Int) =
        withContext(Dispatchers.Default) {
            database.diaryQueries.deleteDiary(year.toLong(), month.toLong(), day.toLong())
        }

    override suspend fun hasDiaryForDate(year: Int, month: Int, day: Int): Boolean =
        withContext(Dispatchers.Default) {
            database.diaryQueries
                .hasDiaryForDate(year.toLong(), month.toLong(), day.toLong())
                .executeAsOne()
        }
}

private fun SQLDelightDiaryEntity.toCommon() = DiaryEntity(
    id = id,
    year = year.toInt(),
    month = month.toInt(),
    day = day.toInt(),
    content = content,
    emoticonId = emoticonId,
    photoUri = photoUri,
    textAlign = textAlign,
    textColor = textColor,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
