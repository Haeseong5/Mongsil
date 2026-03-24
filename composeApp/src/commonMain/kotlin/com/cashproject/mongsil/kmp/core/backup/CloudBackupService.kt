package com.cashproject.mongsil.kmp.core.backup

interface CloudBackupService {
    suspend fun isSignedIn(): Boolean
    suspend fun signIn(): Result<String>
    suspend fun signOut()
    suspend fun upload(data: ByteArray, fileName: String): Result<CloudBackupMetadata>
    suspend fun download(fileId: String): Result<ByteArray>
    suspend fun listBackups(): Result<List<CloudBackupMetadata>>
    suspend fun delete(fileId: String): Result<Unit>
}

data class CloudBackupMetadata(
    val fileId: String,
    val fileName: String,
    val createdAt: String,
    val sizeBytes: Long,
)
