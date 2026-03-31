package com.cashproject.mongsil.kmp.database

import android.content.Context
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import androidx.sqlite.db.SupportSQLiteDatabase

actual class DatabaseDriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        val schema = createSchema()
        return AndroidSqliteDriver(
            schema = schema,
            context = context,
            name = "mongsil2.db",
            callback = object : AndroidSqliteDriver.Callback(schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    db.execSQL("PRAGMA foreign_keys=ON;")
                }
            }
        )
    }

    private fun createSchema(): SqlSchema<QueryResult.Value<Unit>> {
        return object : SqlSchema<QueryResult.Value<Unit>> by MongsilDatabase.Schema {
            override val version: Long = 1

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
