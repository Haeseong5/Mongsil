package com.cashproject.mongsil.kmp.core.backup

import com.cashproject.mongsil.kmp.core.backup.model.BackupConflictPolicy
import com.cashproject.mongsil.kmp.core.backup.model.BackupManifest
import com.cashproject.mongsil.kmp.core.backup.model.RestoreResult

interface BackupRepository {
    suspend fun createBackup(): Result<BackupManifest>
    suspend fun restoreFromManifest(
        manifest: BackupManifest,
        policy: BackupConflictPolicy,
    ): Result<RestoreResult>
}
