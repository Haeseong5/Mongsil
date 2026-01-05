package com.cashproject.mongsil.kmp.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

/**
 * Android용 SQLDelight 드라이버 Factory
 */
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = MongsilDatabase.Schema,
            context = context,
            name = "mongsil.db"
        )
    }
}
