package com.cashproject.mongsil.kmp.core.backup

import androidx.compose.runtime.Composable

@Composable
expect fun CloudBackupSection(
    cloudState: CloudBackupState,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onUpload: () -> Unit,
    onDownload: (String) -> Unit,
    onDelete: (String) -> Unit,
)

data class CloudBackupState(
    val isAvailable: Boolean = false,
    val isSignedIn: Boolean = false,
    val accountName: String = "",
    val isWorking: Boolean = false,
    val workingType: CloudWorkingType? = null,
    val backups: List<CloudBackupMetadata> = emptyList(),
)

enum class CloudWorkingType {
    CREATING_BACKUP,
    UPLOADING,
    DOWNLOADING,
    DELETING,
}
