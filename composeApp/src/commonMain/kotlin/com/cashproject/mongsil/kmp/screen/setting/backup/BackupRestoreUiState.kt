package com.cashproject.mongsil.kmp.screen.setting.backup

import com.cashproject.mongsil.kmp.core.backup.model.BackupConflictPolicy
import com.cashproject.mongsil.kmp.core.backup.model.BackupManifest
import com.cashproject.mongsil.kmp.core.backup.model.BackupOperationResult

data class BackupRestoreUiState(
    val status: BackupScreenStatus = BackupScreenStatus.Idle,
    val selectedPolicy: BackupConflictPolicy = BackupConflictPolicy.Skip,
    val previewManifest: BackupManifest? = null,
    val pendingRestoreBytes: ByteArray? = null,
    val lastResult: BackupOperationResult? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BackupRestoreUiState) return false
        return status == other.status &&
                selectedPolicy == other.selectedPolicy &&
                previewManifest == other.previewManifest &&
                pendingRestoreBytes.contentEquals(other.pendingRestoreBytes) &&
                lastResult == other.lastResult
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + selectedPolicy.hashCode()
        result = 31 * result + (previewManifest?.hashCode() ?: 0)
        result = 31 * result + (pendingRestoreBytes?.contentHashCode() ?: 0)
        result = 31 * result + (lastResult?.hashCode() ?: 0)
        return result
    }
}

sealed class BackupScreenStatus {
    data object Idle : BackupScreenStatus()
    data class Working(val type: WorkingType) : BackupScreenStatus()
    data object Error : BackupScreenStatus()
    data object Success : BackupScreenStatus()
}

enum class WorkingType {
    CREATING_BACKUP,
    RESTORING,
}

private fun ByteArray?.contentEquals(other: ByteArray?): Boolean {
    if (this == null && other == null) return true
    if (this == null || other == null) return false
    return this.contentEquals(other)
}

private fun ByteArray?.contentHashCode(): Int = this?.contentHashCode() ?: 0
