package com.cashproject.mongsil.kmp.database

import app.cash.sqldelight.db.SqlDriver

/**
 * 데이터베이스 마이그레이션 헬퍼
 * 버전별 마이그레이션 로직을 관리합니다.
 */
object DatabaseMigrations {
    
    /**
     * 버전 1에서 2로 마이그레이션
     * DiaryEntity 테이블 추가
     */
    fun migrateV1ToV2(driver: SqlDriver) {
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
    
    /**
     * 버전 2에서 3으로 마이그레이션
     * DiaryEntity 테이블에 emoticonId 컬럼 추가
     */
    fun migrateV2ToV3(driver: SqlDriver) {
        driver.execute(
            null,
            """
            ALTER TABLE DiaryEntity ADD COLUMN emoticonId INTEGER
            """.trimIndent(),
            0
        )
    }
    
    /**
     * 전체 마이그레이션 실행
     * oldVersion에서 newVersion까지 순차적으로 마이그레이션합니다.
     */
    fun migrate(driver: SqlDriver, oldVersion: Long, newVersion: Long) {
        // 버전 1 -> 2
        if (oldVersion < 2 && newVersion >= 2) {
            migrateV1ToV2(driver)
        }
        
        // 버전 2 -> 3
        if (oldVersion < 3 && newVersion >= 3) {
            migrateV2ToV3(driver)
        }
    }
}
