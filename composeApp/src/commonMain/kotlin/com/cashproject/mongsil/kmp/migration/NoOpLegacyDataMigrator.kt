package com.cashproject.mongsil.kmp.migration

class NoOpLegacyDataMigrator : LegacyDataMigrator {
    override fun needsMigration(): Boolean = false
    override suspend fun migrate(): LegacyMigrationResult = LegacyMigrationResult.NotNeeded
}
