package com.cashproject.mongsil.kmp.screen.setting.backup

import com.cashproject.mongsil.kmp.core.backup.BackupRepository
import com.cashproject.mongsil.kmp.core.backup.BackupSerializer
import com.cashproject.mongsil.kmp.core.backup.model.BackupConflictPolicy
import com.cashproject.mongsil.kmp.core.backup.model.BackupManifest
import com.cashproject.mongsil.kmp.core.backup.model.RestoreResult
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.error_unsupported_backup_format
import org.jetbrains.compose.resources.getString

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

    private suspend fun validateManifest(manifest: BackupManifest) {
        val unsupportedMsg = getString(Res.string.error_unsupported_backup_format)
        require(manifest.formatVersion <= BackupManifest.CURRENT_FORMAT_VERSION) {
            unsupportedMsg
        }
    }
}
