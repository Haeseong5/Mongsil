package com.cashproject.mongsil.kmp.screen.setting.backup

import com.cashproject.mongsil.kmp.core.backup.BackupRepository
import com.cashproject.mongsil.kmp.core.backup.BackupSerializer

class CreateBackupUseCase(
    private val backupRepository: BackupRepository,
    private val backupSerializer: BackupSerializer,
) {
    suspend operator fun invoke(): Result<BackupData> = runCatching {
        val manifest = backupRepository.createBackup().getOrThrow()
        val bytes = backupSerializer.serialize(manifest)
        BackupData(bytes = bytes, diaryCount = manifest.diaryCount)
    }
}

data class BackupData(
    val bytes: ByteArray,
    val diaryCount: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BackupData) return false
        return bytes.contentEquals(other.bytes) && diaryCount == other.diaryCount
    }

    override fun hashCode(): Int = bytes.contentHashCode() * 31 + diaryCount
}
