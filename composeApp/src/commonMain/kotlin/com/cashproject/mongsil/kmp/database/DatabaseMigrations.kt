package com.cashproject.mongsil.kmp.database

import app.cash.sqldelight.db.SqlDriver

object DatabaseMigrations {

    private fun migrateV1ToV2(driver: SqlDriver) {
        driver.execute(
            null,
            """
            CREATE TABLE IF NOT EXISTS DiaryEntity (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                year INTEGER NOT NULL,
                month INTEGER NOT NULL,
                day INTEGER NOT NULL,
                content TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
            """.trimIndent(),
            0
        )
    }

    private fun migrateV2ToV3(driver: SqlDriver) {
        driver.execute(null, "ALTER TABLE DiaryEntity ADD COLUMN emoticonId INTEGER", 0)
    }

    private fun migrateV3ToV4(driver: SqlDriver) {
        driver.execute(null, "ALTER TABLE DiaryEntity ADD COLUMN photoUri TEXT", 0)
    }

    private fun migrateV4ToV5(driver: SqlDriver) {
        driver.execute(null, "ALTER TABLE DiaryEntity ADD COLUMN textAlign TEXT NOT NULL DEFAULT 'start'", 0)
    }

    fun migrate(driver: SqlDriver, oldVersion: Long, newVersion: Long) {
        if (oldVersion < 2 && newVersion >= 2) migrateV1ToV2(driver)
        if (oldVersion < 3 && newVersion >= 3) migrateV2ToV3(driver)
        if (oldVersion < 4 && newVersion >= 4) migrateV3ToV4(driver)
        if (oldVersion < 5 && newVersion >= 5) migrateV4ToV5(driver)
    }
}
