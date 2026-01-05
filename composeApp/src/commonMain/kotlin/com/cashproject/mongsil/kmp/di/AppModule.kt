package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.database.DatabaseDriverFactory
import com.cashproject.mongsil.kmp.database.MongsilDatabase
import com.cashproject.mongsil.kmp.repository.CounterRepository
import com.cashproject.mongsil.kmp.viewmodel.CounterViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * 플랫폼별 의존성을 제공하는 모듈
 * Android와 iOS에서 각각 구현됩니다.
 */
expect fun platformModule(): Module

/**
 * 공통 애플리케이션 모듈
 */
val appModule = module {
    // Database
    single {
        val driverFactory = get<DatabaseDriverFactory>()
        MongsilDatabase(driverFactory.createDriver())
    }
    
    // Repository
    single { CounterRepository(get()) }
    
    // ViewModel
    single { CounterViewModel(get()) }
}

/**
 * 전체 Koin 모듈 리스트
 */
fun getKoinModules() = listOf(platformModule(), appModule)
