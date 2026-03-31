package com.cashproject.mongsil.kmp.migration

class NoOpLegacyDataMigrator : LegacyDataMigrator {
    override suspend fun needsMigration(): Boolean = false
    override suspend fun migrate(): LegacyMigrationResult = LegacyMigrationResult.NotNeeded
}
