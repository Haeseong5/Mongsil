package com.cashproject.mongsil.kmp.screen.setting.backup

import com.cashproject.mongsil.kmp.core.backup.BackupConflictPolicy
import com.cashproject.mongsil.kmp.core.backup.RestoreBackupReport

data class BackupRestoreUiState(
    val isWorking: Boolean = false,
    val selectedPolicy: BackupConflictPolicy = BackupConflictPolicy.Merge,
    val lastBackupLocation: String? = null,
    val lastRestoreReport: RestoreBackupReport? = null,
    val progressMessage: String = "",
    val errorMessage: String? = null,
)
