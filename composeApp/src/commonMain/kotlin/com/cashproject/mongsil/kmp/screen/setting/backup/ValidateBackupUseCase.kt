package com.cashproject.mongsil.kmp.screen.setting.backup

import com.cashproject.mongsil.kmp.core.backup.BackupSerializer
import com.cashproject.mongsil.kmp.core.backup.model.BackupManifest
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.error_backup_no_diaries
import mongsil.composeapp.generated.resources.error_unsupported_backup_format
import org.jetbrains.compose.resources.getString

class ValidateBackupUseCase(
    private val backupSerializer: BackupSerializer,
) {
    suspend operator fun invoke(bytes: ByteArray): Result<BackupManifest> = runCatching {
        val manifest = backupSerializer.deserialize(bytes)
        val unsupportedMsg = getString(Res.string.error_unsupported_backup_format)
        val noDiariesMsg = getString(Res.string.error_backup_no_diaries)
        require(manifest.formatVersion <= BackupManifest.CURRENT_FORMAT_VERSION) {
            unsupportedMsg
        }
        require(manifest.diaries.isNotEmpty()) {
            noDiariesMsg
        }
        manifest
    }
}
