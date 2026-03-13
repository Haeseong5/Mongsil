package com.cashproject.mongsil.kmp.database

import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = createSchema(),
            name = "mongsil_v2.db",
        )
    }

    private fun createSchema(): SqlSchema<QueryResult.Value<Unit>> {
        return object : SqlSchema<QueryResult.Value<Unit>> by MongsilDatabase.Schema {
            override val version: Long = 6

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
