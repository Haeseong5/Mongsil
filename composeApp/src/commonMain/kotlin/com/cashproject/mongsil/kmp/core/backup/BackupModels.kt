package com.cashproject.mongsil.kmp.core.backup

import kotlinx.serialization.Serializable

@Serializable
data class BackupManifest(
    val formatVersion: Int,
    val appVersion: String,
    val createdAtIso: String,
    val diaries: List<BackupDiary>,
)

@Serializable
data class BackupDiary(
    val date: String,
    val content: String,
    val emoticonId: Long? = null,
    val textAlign: String = "start",
    val textColor: String = "FF000000",
    val backgroundColor: String = "00000000",
    val createdAt: Long,
    val updatedAt: Long,
    val mediaFiles: List<BackupMediaFile> = emptyList(),
)

@Serializable
data class BackupMediaFile(
    val name: String,
    val mimeType: String? = null,
)

enum class BackupConflictPolicy {
    Skip,
    Overwrite,
    Merge,
}

data class RestoreBackupReport(
    val importedCount: Int,
    val skippedCount: Int,
    val mergedCount: Int,
    val failedCount: Int,
    val failedItems: List<RestoreFailureItem>,
)

data class RestoreFailureItem(
    val date: String,
    val reason: String,
)

data class CreateBackupRequest(
    val targetLocation: String? = null,
)

data class RestoreBackupRequest(
    val backupLocation: String,
    val conflictPolicy: BackupConflictPolicy,
)

data class BackupCreationResult(
    val backupLocation: String,
    val diaryCount: Int,
)
