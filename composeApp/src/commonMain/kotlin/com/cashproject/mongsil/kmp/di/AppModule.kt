package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.viewmodel.CounterViewModel
import org.koin.dsl.module

/**
 * Koin 의존성 주입을 위한 애플리케이션 모듈
 */
val appModule = module {
    // CounterViewModel을 싱글톤으로 등록
    single { CounterViewModel() }
}

/**
 * 전체 Koin 모듈 리스트
 * 새로운 모듈 추가 시 이 리스트에 포함시킵니다.
 */
fun getKoinModules() = listOf(appModule)
