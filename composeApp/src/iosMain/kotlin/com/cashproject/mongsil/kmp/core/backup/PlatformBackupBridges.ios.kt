package com.cashproject.mongsil.kmp.core.backup

actual class PlatformBackupFileStore : BackupFileStore {
    actual override suspend fun writeBackup(data: ByteArray, targetLocation: String?): String {
        throw UnsupportedOperationException("iOS BackupFileStore는 아직 구현되지 않았습니다.")
    }

    actual override suspend fun readBackup(location: String): ByteArray {
        throw UnsupportedOperationException("iOS BackupFileStore는 아직 구현되지 않았습니다.")
    }
}
