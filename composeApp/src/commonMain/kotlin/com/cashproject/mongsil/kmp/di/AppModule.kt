package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.database.DatabaseDriverFactory
import com.cashproject.mongsil.kmp.database.MongsilDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * 플랫폼별 모듈 (expect/actual)
 * Android: DatabaseDriverFactory, 기타 Android 의존성
 * iOS: DatabaseDriverFactory, 기타 iOS 의존성
 */
expect fun platformModule(): Module

/**
 * Database 모듈
 */
internal val databaseModule = module {
    // MongsilDatabase 생성
    single {
        val driverFactory: DatabaseDriverFactory = get()
        MongsilDatabase(driverFactory.createDriver())
    }
}

/**
 * Repository 전용 모듈
 */
internal val repositoryModule = module {
    // Repository들 등록
    single { com.cashproject.mongsil.kmp.repository.DiaryRepository(get()) }
}

/**
 * 앱 전체 모듈 통합
 * 각 Feature별 모듈을 여기에 포함시킵니다
 */
val appModules: List<Module> = listOf(
    platformModule(),    // 플랫폼별 의존성 (DatabaseDriverFactory)
    databaseModule,      // Database 인스턴스 생성
    networkModule,       // Network (Ktor, API, Remote Repositories)
    repositoryModule,    // Local Repositories
    calendarModule,
    counterModule,
    diaryModule,
    // 새로운 feature 모듈을 여기에 추가
)
