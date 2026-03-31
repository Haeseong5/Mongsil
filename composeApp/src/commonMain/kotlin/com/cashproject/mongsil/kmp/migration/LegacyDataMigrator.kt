package com.cashproject.mongsil.kmp.migration

interface LegacyDataMigrator {
    suspend fun needsMigration(): Boolean
    suspend fun migrate(): LegacyMigrationResult
}
