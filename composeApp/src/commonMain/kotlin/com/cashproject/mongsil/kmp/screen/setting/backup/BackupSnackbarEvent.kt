package com.cashproject.mongsil.kmp.screen.setting.backup

sealed interface BackupSnackbarEvent {
    data class BackupCreated(val count: Int) : BackupSnackbarEvent
    data object BackupFailed : BackupSnackbarEvent
    data object FileSaved : BackupSnackbarEvent
    data object InvalidFile : BackupSnackbarEvent
    data class RestoreCompleted(val importedCount: Int) : BackupSnackbarEvent
    data object RestoreFailed : BackupSnackbarEvent
    data class SignedIn(val name: String) : BackupSnackbarEvent
    data object SignInFailed : BackupSnackbarEvent
    data object CloudUploadSuccess : BackupSnackbarEvent
    data object CloudUploadFailed : BackupSnackbarEvent
    data object CloudDownloadSuccess : BackupSnackbarEvent
    data object CloudDownloadFailed : BackupSnackbarEvent
    data object CloudDeleteSuccess : BackupSnackbarEvent
    data object CloudDeleteFailed : BackupSnackbarEvent
}
