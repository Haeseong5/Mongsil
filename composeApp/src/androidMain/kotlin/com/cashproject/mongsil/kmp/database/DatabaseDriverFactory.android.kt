package com.cashproject.mongsil.kmp.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

/**
 * Android용 SQLDelight 드라이버 Factory
 */
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
                    // 외래 키 제약 조건 활성화
                    db.execSQL("PRAGMA foreign_keys=ON;")
                }
            }
        )
    }
    
    /**
     * 마이그레이션이 포함된 스키마 생성
     */
    private fun createSchema(): SqlSchema<QueryResult.Value<Unit>> {
        return object : SqlSchema<QueryResult.Value<Unit>> by MongsilDatabase.Schema {
            override val version: Long = 3
            
            override fun migrate(
                driver: SqlDriver,
                oldVersion: Long,
                newVersion: Long,
                vararg callbacks: AfterVersion
            ): QueryResult.Value<Unit> {
                DatabaseMigrations.migrate(driver, oldVersion, newVersion)
                return QueryResult.Unit
            }
        }
    }
}
