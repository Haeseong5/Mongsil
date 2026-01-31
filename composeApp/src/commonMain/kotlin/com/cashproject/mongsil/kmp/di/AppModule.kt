package com.cashproject.mongsil.kmp.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * 플랫폼별 모듈 (expect/actual)
 * Android: DatabaseDriverFactory, 기타 Android 의존성
 * iOS: DatabaseDriverFactory, 기타 iOS 의존성
 */
expect fun platformModule(): Module

/**
 * Repository 전용 모듈
 */
internal val repositoryModule = module {
    // Repository들 등록
    // single<CheckInRepository> { CheckInRepositoryImpl(get(), get()) }
}

/**
 * 앱 전체 모듈 통합
 * 각 Feature별 모듈을 여기에 포함시킵니다
 */
val appModules: List<Module> = listOf(
    platformModule(),    // 플랫폼별 의존성
    repositoryModule,
    calendarModule,
    counterModule,
    // 새로운 feature 모듈을 여기에 추가
)
