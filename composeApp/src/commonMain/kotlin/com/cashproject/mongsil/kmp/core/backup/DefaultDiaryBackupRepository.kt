package com.cashproject.mongsil.kmp.core.backup

import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.serialization.json.Json

@OptIn(ExperimentalTime::class)
class DefaultDiaryBackupRepository(
    private val diaryRepository: DiaryRepository,
    private val backupFileStore: BackupFileStore,
    private val json: Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    },
) : DiaryBackupRepository {

    override suspend fun createBackup(request: CreateBackupRequest): Result<BackupCreationResult> = runCatching {
        val diaries = diaryRepository.getAllDiaries().sortedWith(compareBy({ it.year }, { it.month }, { it.day }))

        val manifest = BackupManifest(
            formatVersion = CURRENT_FORMAT_VERSION,
            appVersion = "unknown",
            createdAtIso = Clock.System.now().toString(),
            diaries = diaries.map { diary ->
                BackupDiary(
                    date = diaryDateString(diary.year, diary.month, diary.day),
                    content = diary.content,
                    emoticonId = diary.emoticonId,
                    textAlign = diary.textAlign,
                    textColor = diary.textColor,
                    backgroundColor = diary.backgroundColor,
                    createdAt = diary.createdAt,
                    updatedAt = diary.updatedAt,
                    mediaFiles = deserializeMediaFiles(diary.photoUri),
                )
            },
        )

        val plainBytes = json.encodeToString(BackupManifest.serializer(), manifest).encodeToByteArray()
        val location = backupFileStore.writeBackup(
            data = plainBytes,
            targetLocation = request.targetLocation,
        )

        BackupCreationResult(
            backupLocation = location,
            diaryCount = manifest.diaries.size,
        )
    }

    override suspend fun restoreBackup(request: RestoreBackupRequest): Result<RestoreBackupReport> = runCatching {
        val plainBytes = backupFileStore.readBackup(request.backupLocation)
        val manifest = json.decodeFromString(BackupManifest.serializer(), plainBytes.decodeToString())

        require(manifest.formatVersion <= CURRENT_FORMAT_VERSION) {
            "지원되지 않는 백업 파일 버전입니다. formatVersion=${manifest.formatVersion}"
        }

        var importedCount = 0
        var skippedCount = 0
        var mergedCount = 0
        val failedItems = mutableListOf<RestoreFailureItem>()

        manifest.diaries.forEach { backupDiary ->
            runCatching {
                val (year, month, day) = parseDate(backupDiary.date)
                val localDiary = diaryRepository.getDiaryByDate(year, month, day)

                when {
                    localDiary == null -> {
                        diaryRepository.saveDiary(
                            year = year,
                            month = month,
                            day = day,
                            content = backupDiary.content,
                            emoticonId = backupDiary.emoticonId,
                            photoUri = serializeMediaFiles(backupDiary.mediaFiles),
                            textAlign = backupDiary.textAlign,
                            textColor = backupDiary.textColor,
                            backgroundColor = backupDiary.backgroundColor,
                        ).getOrThrow()
                        importedCount += 1
                    }

                    request.conflictPolicy == BackupConflictPolicy.Skip -> {
                        skippedCount += 1
                    }

                    request.conflictPolicy == BackupConflictPolicy.Overwrite -> {
                        diaryRepository.saveDiary(
                            year = year,
                            month = month,
                            day = day,
                            content = backupDiary.content,
                            emoticonId = backupDiary.emoticonId,
                            photoUri = serializeMediaFiles(backupDiary.mediaFiles),
                            textAlign = backupDiary.textAlign,
                            textColor = backupDiary.textColor,
                            backgroundColor = backupDiary.backgroundColor,
                        ).getOrThrow()
                        importedCount += 1
                    }

                    else -> {
                        val merged = mergeDiary(localDiary.content, localDiary.photoUri, backupDiary)
                        diaryRepository.saveDiary(
                            year = year,
                            month = month,
                            day = day,
                            content = merged.content,
                            emoticonId = backupDiary.emoticonId ?: localDiary.emoticonId,
                            photoUri = merged.photoUri,
                            textAlign = selectStyle(
                                localValue = localDiary.textAlign,
                                backupValue = backupDiary.textAlign,
                                localUpdatedAt = localDiary.updatedAt,
                                backupUpdatedAt = backupDiary.updatedAt,
                            ),
                            textColor = selectStyle(
                                localValue = localDiary.textColor,
                                backupValue = backupDiary.textColor,
                                localUpdatedAt = localDiary.updatedAt,
                                backupUpdatedAt = backupDiary.updatedAt,
                            ),
                            backgroundColor = selectStyle(
                                localValue = localDiary.backgroundColor,
                                backupValue = backupDiary.backgroundColor,
                                localUpdatedAt = localDiary.updatedAt,
                                backupUpdatedAt = backupDiary.updatedAt,
                            ),
                        ).getOrThrow()
                        mergedCount += 1
                    }
                }
            }.onFailure { throwable ->
                failedItems += RestoreFailureItem(
                    date = backupDiary.date,
                    reason = throwable.message ?: "알 수 없는 오류",
                )
            }
        }

        RestoreBackupReport(
            importedCount = importedCount,
            skippedCount = skippedCount,
            mergedCount = mergedCount,
            failedCount = failedItems.size,
            failedItems = failedItems,
        )
    }

    private data class MergedDiaryResult(
        val content: String,
        val photoUri: String?,
    )

    private fun mergeDiary(localContent: String, localPhotoUri: String?, backupDiary: BackupDiary): MergedDiaryResult {
        val mergedContent = mergeContent(
            backupContent = backupDiary.content,
            localContent = localContent,
        )
        val mergedPhotoUri = mergePhotos(
            backupMedia = backupDiary.mediaFiles,
            localPhotoUri = localPhotoUri,
        )

        return MergedDiaryResult(
            content = mergedContent,
            photoUri = mergedPhotoUri,
        )
    }

    private fun mergeContent(backupContent: String, localContent: String): String {
        val backupText = backupContent.trim()
        val localText = localContent.trim()

        if (backupText.isBlank()) return localText
        if (localText.isBlank()) return backupText
        if (backupText == localText) return localText

        return buildString {
            append(backupText)
            append("\n\n---\n\n")
            append(localText)
        }
    }

    private fun mergePhotos(backupMedia: List<BackupMediaFile>, localPhotoUri: String?): String? {
        val backupNames = backupMedia.map { it.name }
        val localNames = localPhotoUri
            ?.split(',')
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            .orEmpty()

        val merged = (backupNames + localNames).distinct()
        return if (merged.isEmpty()) null else merged.joinToString(separator = ",")
    }

    private fun selectStyle(
        localValue: String,
        backupValue: String,
        localUpdatedAt: Long,
        backupUpdatedAt: Long,
    ): String = if (backupUpdatedAt >= localUpdatedAt) backupValue else localValue

    private fun deserializeMediaFiles(photoUri: String?): List<BackupMediaFile> {
        if (photoUri.isNullOrBlank()) return emptyList()
        return photoUri.split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { BackupMediaFile(name = it) }
    }

    private fun serializeMediaFiles(mediaFiles: List<BackupMediaFile>): String? {
        val names = mediaFiles.map { it.name.trim() }.filter { it.isNotEmpty() }
        return if (names.isEmpty()) null else names.joinToString(separator = ",")
    }

    private fun parseDate(date: String): Triple<Int, Int, Int> {
        val parts = date.split('-')
        require(parts.size == 3) { "올바르지 않은 날짜 형식입니다: $date" }
        return Triple(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
    }

    private fun diaryDateString(year: Int, month: Int, day: Int): String =
        buildString {
            append(year.toString().padStart(4, '0'))
            append('-')
            append(month.toString().padStart(2, '0'))
            append('-')
            append(day.toString().padStart(2, '0'))
        }

    companion object {
        private const val CURRENT_FORMAT_VERSION = 1
    }
}
