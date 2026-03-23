package com.cashproject.mongsil.kmp.screen.setting.backup

import com.cashproject.mongsil.kmp.core.backup.BackupCreationResult
import com.cashproject.mongsil.kmp.core.backup.CreateBackupRequest
import com.cashproject.mongsil.kmp.core.backup.DiaryBackupRepository

class CreateBackupUseCase(
    private val backupRepository: DiaryBackupRepository,
) {
    suspend operator fun invoke(targetLocation: String? = null): Result<BackupCreationResult> {
        return backupRepository.createBackup(CreateBackupRequest(targetLocation = targetLocation))
    }
}
