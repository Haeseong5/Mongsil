package com.cashproject.mongsil.kmp.core.backup.model

sealed class BackupConflictPolicy {
    data object Skip : BackupConflictPolicy()
    data object Overwrite : BackupConflictPolicy()
    data object MergeAppend : BackupConflictPolicy()
}
