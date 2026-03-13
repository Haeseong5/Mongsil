package com.cashproject.mongsil.kmp.database

import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        val dbFile = File(desktopDatabaseDir(), "mongsil_v2.db")
        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
        createSchema().create(driver)
        return driver
    }

    private fun createSchema(): SqlSchema<QueryResult.Value<Unit>> {
        return object : SqlSchema<QueryResult.Value<Unit>> by MongsilDatabase.Schema {
            override val version: Long = 5

            override fun migrate(
                driver: SqlDriver,
                oldVersion: Long,
                newVersion: Long,
                vararg callbacks: AfterVersion,
            ): QueryResult.Value<Unit> {
                DatabaseMigrations.migrate(driver, oldVersion, newVersion)
                return QueryResult.Unit
            }
        }
    }
}

private fun desktopDatabaseDir(): File {
    val dir = File(System.getProperty("user.home"), ".mongsil")
    if (!dir.exists()) dir.mkdirs()
    return dir
}
