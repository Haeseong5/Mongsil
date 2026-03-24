package com.cashproject.mongsil.kmp.core.backup

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class GoogleDriveBackupService(
    private val context: Context,
) : CloudBackupService {

    private var driveService: Drive? = null

    override suspend fun isSignedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account != null && hasDriveScope(account)
    }

    override suspend fun signIn(): Result<String> = runCatching {
        val account = GoogleSignIn.getLastSignedInAccount(context)
            ?: throw GoogleSignInRequiredException()
        if (!hasDriveScope(account)) throw GoogleSignInRequiredException()
        initDriveService(account)
        account.email ?: account.displayName ?: "Google"
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) {
            val client = GoogleSignIn.getClient(context, buildSignInOptions())
            client.signOut()
            driveService = null
        }
    }

    override suspend fun upload(
        data: ByteArray,
        fileName: String,
    ): Result<CloudBackupMetadata> = runCatching {
        val drive = getDriveService()

        withContext(Dispatchers.IO) {
            val existing = findFileByName(drive, fileName)
            val content = ByteArrayContent("application/json", data)

            val file = if (existing != null) {
                drive.files().update(existing.id, null, content).execute()
            } else {
                val metadata = com.google.api.services.drive.model.File().apply {
                    name = fileName
                    parents = listOf(APP_DATA_FOLDER)
                    mimeType = "application/json"
                }
                drive.files().create(metadata, content)
                    .setFields("id,name,createdTime,size")
                    .execute()
            }

            CloudBackupMetadata(
                fileId = file.id,
                fileName = file.name,
                createdAt = file.createdTime?.toString().orEmpty(),
                sizeBytes = file.getSize()?.toLong() ?: data.size.toLong(),
            )
        }
    }

    override suspend fun download(fileId: String): Result<ByteArray> = runCatching {
        val drive = getDriveService()
        withContext(Dispatchers.IO) {
            val outputStream = ByteArrayOutputStream()
            drive.files().get(fileId).executeMediaAndDownloadTo(outputStream)
            outputStream.toByteArray()
        }
    }

    override suspend fun listBackups(): Result<List<CloudBackupMetadata>> = runCatching {
        val drive = getDriveService()
        withContext(Dispatchers.IO) {
            val result = drive.files().list()
                .setSpaces(APP_DATA_FOLDER)
                .setFields("files(id,name,createdTime,size)")
                .setOrderBy("createdTime desc")
                .execute()

            result.files?.map { file ->
                CloudBackupMetadata(
                    fileId = file.id,
                    fileName = file.name,
                    createdAt = file.createdTime?.toString().orEmpty(),
                    sizeBytes = file.getSize()?.toLong() ?: 0L,
                )
            }.orEmpty()
        }
    }

    override suspend fun delete(fileId: String): Result<Unit> = runCatching {
        val drive = getDriveService()
        withContext(Dispatchers.IO) {
            drive.files().delete(fileId).execute()
        }
    }

    fun initWithAccount(account: GoogleSignInAccount) {
        initDriveService(account)
    }

    private fun initDriveService(account: GoogleSignInAccount) {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(DriveScopes.DRIVE_APPDATA),
        )
        credential.selectedAccount = account.account
        driveService = Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential,
        ).setApplicationName(APP_NAME).build()
    }

    private fun getDriveService(): Drive {
        return driveService ?: throw IllegalStateException("Google Drive 로그인이 필요합니다.")
    }

    private fun findFileByName(
        drive: Drive,
        name: String,
    ): com.google.api.services.drive.model.File? {
        val result = drive.files().list()
            .setSpaces(APP_DATA_FOLDER)
            .setQ("name = '$name'")
            .setFields("files(id,name)")
            .execute()
        return result.files?.firstOrNull()
    }

    private fun hasDriveScope(account: GoogleSignInAccount): Boolean {
        return account.grantedScopes.contains(Scope(DriveScopes.DRIVE_APPDATA))
    }

    companion object {
        private const val APP_DATA_FOLDER = "appDataFolder"
        private const val APP_NAME = "Mongsil"

        fun buildSignInOptions(): GoogleSignInOptions {
            return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
                .build()
        }
    }
}

class GoogleSignInRequiredException : Exception("Google 로그인이 필요합니다.")
