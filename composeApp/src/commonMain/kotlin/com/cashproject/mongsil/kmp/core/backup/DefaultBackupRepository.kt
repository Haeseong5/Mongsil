package com.cashproject.mongsil.kmp.core.backup

import com.cashproject.mongsil.kmp.core.backup.model.BackupConflictPolicy
import com.cashproject.mongsil.kmp.core.backup.model.BackupDiary
import com.cashproject.mongsil.kmp.core.backup.model.BackupManifest
import com.cashproject.mongsil.kmp.core.backup.model.RestoreFailureItem
import com.cashproject.mongsil.kmp.core.backup.model.RestoreResult
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.error_invalid_date_format
import org.jetbrains.compose.resources.getString
import kotlin.time.Clock

class DefaultBackupRepository(
    private val diaryRepository: DiaryRepository,
) : BackupRepository {

    override suspend fun createBackup(): Result<BackupManifest> = runCatching {
        val diaries = diaryRepository.getAllDiaries()
        val backupDiaries = diaries.map { it.toBackupDiary() }
        val now = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())

        BackupManifest(
            appVersion = APP_VERSION,
            createdAtIso = now.toString(),
            platformName = getPlatformNameForBackup(),
            diaryCount = backupDiaries.size,
            diaries = backupDiaries,
        )
    }

    override suspend fun restoreFromManifest(
        manifest: BackupManifest,
        policy: BackupConflictPolicy,
    ): Result<RestoreResult> = runCatching {
        var imported = 0
        var skipped = 0
        var merged = 0
        var failed = 0
        val failures = mutableListOf<RestoreFailureItem>()

        for (diary in manifest.diaries) {
            val result = restoreSingleDiary(diary, policy)
            when (result) {
                RestoreSingleResult.IMPORTED -> imported++
                RestoreSingleResult.SKIPPED -> skipped++
                RestoreSingleResult.MERGED -> merged++
                is RestoreSingleResult.FAILED -> {
                    failed++
                    failures.add(RestoreFailureItem(diary.date, result.reason))
                }
            }
        }

        RestoreResult(imported, skipped, merged, failed, failures)
    }

    private suspend fun restoreSingleDiary(
        diary: BackupDiary,
        policy: BackupConflictPolicy,
    ): RestoreSingleResult {
        val parsed = parseDateString(diary.date) ?: return RestoreSingleResult.FAILED(
            reason = getString(Res.string.error_invalid_date_format, diary.date)
        )
        val existing = diaryRepository.getDiaryByDate(parsed.year, parsed.month, parsed.day)

        return when {
            existing == null -> {
                saveDiaryFromBackup(parsed, diary)
                RestoreSingleResult.IMPORTED
            }

            policy is BackupConflictPolicy.Skip -> RestoreSingleResult.SKIPPED
            policy is BackupConflictPolicy.Overwrite -> {
                saveDiaryFromBackup(parsed, diary)
                RestoreSingleResult.IMPORTED
            }

            policy is BackupConflictPolicy.MergeAppend -> {
                mergeDiary(parsed, existing, diary)
                RestoreSingleResult.MERGED
            }

            else -> RestoreSingleResult.SKIPPED
        }
    }

    private suspend fun saveDiaryFromBackup(
        date: ParsedDate,
        diary: BackupDiary,
    ) {
        diaryRepository.saveDiary(
            year = date.year,
            month = date.month,
            day = date.day,
            content = diary.content,
            emoticonId = diary.emoticonId,
            textAlign = diary.textAlign,
            textColor = diary.textColor,
            backgroundColor = diary.backgroundColor,
        )
    }

    private suspend fun mergeDiary(
        date: ParsedDate,
        existing: DiaryEntity,
        backup: BackupDiary,
    ) {
        val mergedContent = "${existing.content}\n---\n${backup.content}"
        val useBackupStyle = backup.updatedAt > existing.updatedAt

        diaryRepository.saveDiary(
            year = date.year,
            month = date.month,
            day = date.day,
            content = mergedContent,
            emoticonId = existing.emoticonId ?: backup.emoticonId,
            textAlign = if (useBackupStyle) backup.textAlign else existing.textAlign,
            textColor = if (useBackupStyle) backup.textColor else existing.textColor,
            backgroundColor = if (useBackupStyle) backup.backgroundColor else existing.backgroundColor,
        )
    }

    private fun parseDateString(date: String): ParsedDate? {
        val parts = date.split("-")
        if (parts.size != 3) return null
        val year = parts[0].toIntOrNull() ?: return null
        val month = parts[1].toIntOrNull() ?: return null
        val day = parts[2].toIntOrNull() ?: return null
        return ParsedDate(year, month, day)
    }

    companion object {
        private const val APP_VERSION = "2.0.0"
    }
}

private fun DiaryEntity.toBackupDiary(): BackupDiary = BackupDiary(
    date = "${year.toString().padStart(4, '0')}-${
        month.toString().padStart(2, '0')
    }-${day.toString().padStart(2, '0')}",
    content = content,
    emoticonId = emoticonId,
    textAlign = textAlign,
    textColor = textColor,
    backgroundColor = backgroundColor,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

private data class ParsedDate(val year: Int, val month: Int, val day: Int)

private sealed class RestoreSingleResult {
    data object IMPORTED : RestoreSingleResult()
    data object SKIPPED : RestoreSingleResult()
    data object MERGED : RestoreSingleResult()
    data class FAILED(val reason: String) : RestoreSingleResult()
}

expect fun getPlatformNameForBackup(): String
