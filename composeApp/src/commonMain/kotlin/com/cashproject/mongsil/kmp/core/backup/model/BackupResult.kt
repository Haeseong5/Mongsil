package com.cashproject.mongsil.kmp.core.backup.model

sealed class BackupOperationResult {
    data class Created(val diaryCount: Int) : BackupOperationResult()
    data class Restored(val result: RestoreResult) : BackupOperationResult()
}

data class RestoreResult(
    val imported: Int,
    val skipped: Int,
    val merged: Int,
    val failed: Int,
    val failures: List<RestoreFailureItem> = emptyList(),
)

data class RestoreFailureItem(
    val date: String,
    val reason: String,
)
