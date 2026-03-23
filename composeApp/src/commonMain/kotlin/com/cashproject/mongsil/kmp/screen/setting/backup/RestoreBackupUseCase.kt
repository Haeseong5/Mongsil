package com.cashproject.mongsil.kmp.screen.setting.backup

import com.cashproject.mongsil.kmp.core.backup.BackupRepository
import com.cashproject.mongsil.kmp.core.backup.BackupSerializer
import com.cashproject.mongsil.kmp.core.backup.model.BackupConflictPolicy
import com.cashproject.mongsil.kmp.core.backup.model.BackupManifest
import com.cashproject.mongsil.kmp.core.backup.model.RestoreResult

class RestoreBackupUseCase(
    private val backupRepository: BackupRepository,
    private val backupSerializer: BackupSerializer,
) {
    suspend operator fun invoke(
        bytes: ByteArray,
        policy: BackupConflictPolicy,
    ): Result<RestoreResult> = runCatching {
        val manifest = backupSerializer.deserialize(bytes)
        validateManifest(manifest)
        backupRepository.restoreFromManifest(manifest, policy).getOrThrow()
    }

    private fun validateManifest(manifest: BackupManifest) {
        require(manifest.formatVersion <= BackupManifest.CURRENT_FORMAT_VERSION) {
            "지원하지 않는 백업 형식입니다. 앱을 업데이트 해주세요."
        }
    }
}
