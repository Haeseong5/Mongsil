package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.repository.CounterRepository
import com.cashproject.mongsil.kmp.screen.counter.CounterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * 카운터 Feature 모듈
 * 카운터 관련 ViewModel, Repository 등록
 */
internal val counterModule = module {
    // Repository
    single<CounterRepository> { CounterRepository(get()) }
    
    // ViewModel
    viewModelOf(::CounterViewModel)
}
