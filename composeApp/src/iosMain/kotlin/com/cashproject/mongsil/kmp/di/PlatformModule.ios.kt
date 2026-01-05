package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.database.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS 플랫폼 의존성 모듈
 */
actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory() }
}
