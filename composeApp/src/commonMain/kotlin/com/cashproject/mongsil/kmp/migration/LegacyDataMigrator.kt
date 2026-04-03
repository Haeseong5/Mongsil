package com.cashproject.mongsil.kmp.migration

interface LegacyDataMigrator {
    fun needsMigration(): Boolean
    suspend fun migrate(): LegacyMigrationResult
}
