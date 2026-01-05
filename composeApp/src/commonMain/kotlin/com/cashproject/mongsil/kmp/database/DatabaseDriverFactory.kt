package com.cashproject.mongsil.kmp.database

import app.cash.sqldelight.db.SqlDriver

/**
 * 플랫폼별 SQLDelight 드라이버를 생성하는 Factory
 * Android와 iOS에서 각각 구현됩니다.
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
