package com.cashproject.mongsil.kmp.screen.setting.backup

import com.cashproject.mongsil.kmp.core.backup.BackupConflictPolicy
import com.cashproject.mongsil.kmp.core.backup.DiaryBackupRepository
import com.cashproject.mongsil.kmp.core.backup.RestoreBackupReport
import com.cashproject.mongsil.kmp.core.backup.RestoreBackupRequest

class RestoreBackupUseCase(
    private val backupRepository: DiaryBackupRepository,
) {
    suspend operator fun invoke(
        backupLocation: String,
        conflictPolicy: BackupConflictPolicy,
    ): Result<RestoreBackupReport> {
        return backupRepository.restoreBackup(
            RestoreBackupRequest(
                backupLocation = backupLocation,
                conflictPolicy = conflictPolicy,
            ),
        )
    }
}
