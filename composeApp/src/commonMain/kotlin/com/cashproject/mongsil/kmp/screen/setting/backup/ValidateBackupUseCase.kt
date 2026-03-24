package com.cashproject.mongsil.kmp.screen.setting.backup

import com.cashproject.mongsil.kmp.core.backup.BackupSerializer
import com.cashproject.mongsil.kmp.core.backup.model.BackupManifest

class ValidateBackupUseCase(
    private val backupSerializer: BackupSerializer,
) {
    operator fun invoke(bytes: ByteArray): Result<BackupManifest> = runCatching {
        val manifest = backupSerializer.deserialize(bytes)
        require(manifest.formatVersion <= BackupManifest.CURRENT_FORMAT_VERSION) {
            "지원하지 않는 백업 형식입니다. 앱을 업데이트 해주세요."
        }
        require(manifest.diaries.isNotEmpty()) {
            "백업 파일에 일기 데이터가 없습니다."
        }
        manifest
    }
}
