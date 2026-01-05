package com.cashproject.mongsil.kmp.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

/**
 * iOS용 SQLDelight 드라이버 Factory
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = MongsilDatabase.Schema,
            name = "mongsil.db"
        )
    }
}
