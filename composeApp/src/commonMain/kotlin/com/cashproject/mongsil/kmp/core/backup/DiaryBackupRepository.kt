package com.cashproject.mongsil.kmp.core.backup

interface DiaryBackupRepository {
    suspend fun createBackup(request: CreateBackupRequest): Result<BackupCreationResult>
    suspend fun restoreBackup(request: RestoreBackupRequest): Result<RestoreBackupReport>
}
