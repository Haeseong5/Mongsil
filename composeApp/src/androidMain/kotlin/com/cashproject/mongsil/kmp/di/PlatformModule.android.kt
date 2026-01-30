package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.database.DatabaseDriverFactory
import com.cashproject.mongsil.kmp.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android 플랫폼 의존성 모듈
 */
actual fun platformModule(): Module = module {
    single {
        DatabaseDriverFactory(get())
    }
}