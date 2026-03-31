package com.cashproject.mongsil.kmp.migration

sealed class LegacyMigrationResult {
    data object NotNeeded : LegacyMigrationResult()
    data class Success(val count: Int, val durationMs: Long) : LegacyMigrationResult()
    data class Failed(val error: Throwable) : LegacyMigrationResult()
}
