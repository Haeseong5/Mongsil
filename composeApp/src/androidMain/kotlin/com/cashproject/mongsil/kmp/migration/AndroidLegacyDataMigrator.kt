package com.cashproject.mongsil.kmp.migration

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.cashproject.mongsil.kmp.database.dao.DiaryDao
import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import com.cashproject.mongsil.kmp.firebase.FirebaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class AndroidLegacyDataMigrator(
    private val context: Context,
    private val diaryDao: DiaryDao,
    private val firebaseService: FirebaseService,
) : LegacyDataMigrator {

    override suspend fun needsMigration(): Boolean = withContext(Dispatchers.IO) {
        context.getDatabasePath(LEGACY_DB_NAME).exists()
    }

    override suspend fun migrate(): LegacyMigrationResult = withContext(Dispatchers.IO) {
        val dbFile = context.getDatabasePath(LEGACY_DB_NAME)
        if (!dbFile.exists()) return@withContext LegacyMigrationResult.NotNeeded

        val startTime = System.currentTimeMillis()

        try {
            val comments = readLegacyComments(dbFile.absolutePath)
            if (comments.isEmpty()) {
                deleteLegacyDatabase()
                return@withContext LegacyMigrationResult.Success(
                    count = 0,
                    durationMs = System.currentTimeMillis() - startTime
                )
            }

            val diaries = mergeByDate(comments)

            for (diary in diaries) {
                val existing = diaryDao.countDiaryForDate(diary.year, diary.month, diary.day)
                if (existing == 0.toLong()) {
                    diaryDao.insertOrUpdate(diary)
                }
            }

            deleteLegacyDatabase()

            val durationMs = System.currentTimeMillis() - startTime
            firebaseService.logEvent(
                name = "legacy_db_migration",
                params = mapOf(
                    "migrated_count" to diaries.size.toString(),
                    "duration_ms" to durationMs.toString(),
                )
            )

            LegacyMigrationResult.Success(count = diaries.size, durationMs = durationMs)
        } catch (e: Exception) {
            firebaseService.recordException(e)
            LegacyMigrationResult.Failed(e)
        }
    }

    private fun readLegacyComments(dbPath: String): List<LegacyComment> {
        val comments = mutableListOf<LegacyComment>()
        val db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)

        db.use {
            val cursor = it.rawQuery(
                "SELECT id, content, emotion, time, date FROM Comment ORDER BY time ASC",
                null
            )
            cursor.use { c ->
                while (c.moveToNext()) {
                    comments.add(
                        LegacyComment(
                            id = c.getInt(0),
                            content = c.getString(1),
                            emotion = c.getInt(2),
                            writeTime = c.getLong(3),
                            date = c.getLong(4),
                        )
                    )
                }
            }
        }

        return comments
    }

    private fun mergeByDate(comments: List<LegacyComment>): List<DiaryEntity> {
        val calendar = Calendar.getInstance()

        return comments
            .groupBy { it.date }
            .map { (_, group) ->
                val sorted = group.sortedBy { it.writeTime }
                val latest = sorted.last()

                calendar.timeInMillis = latest.date
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val mergedContent = sorted.joinToString(separator = "\n") { it.content }

                DiaryEntity(
                    year = year,
                    month = month,
                    day = day,
                    content = mergedContent,
                    emoticonId = latest.emotion.toLong(),
                    createdAt = latest.writeTime,
                    updatedAt = latest.writeTime,
                )
            }
    }

    private fun deleteLegacyDatabase() {
        context.deleteDatabase(LEGACY_DB_NAME)
    }

    private data class LegacyComment(
        val id: Int,
        val content: String,
        val emotion: Int,
        val writeTime: Long,
        val date: Long,
    )

    companion object {
        private const val LEGACY_DB_NAME = "Mongsil.db"
    }
}
