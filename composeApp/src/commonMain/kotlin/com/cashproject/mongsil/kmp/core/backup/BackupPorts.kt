package com.cashproject.mongsil.kmp.core.backup

interface BackupFileStore {
    suspend fun writeBackup(data: ByteArray, targetLocation: String? = null): String
    suspend fun readBackup(location: String): ByteArray
}

expect class PlatformBackupFileStore() : BackupFileStore {
    override suspend fun writeBackup(data: ByteArray, targetLocation: String?): String
    override suspend fun readBackup(location: String): ByteArray
}
