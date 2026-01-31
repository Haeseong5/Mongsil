package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.screen.calendar.CalendarViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * 캘린더 Feature 모듈
 * 캘린더 관련 ViewModel, Repository, UseCase 등록
 */
internal val calendarModule = module {
    // ViewModel
    viewModelOf(::CalendarViewModel)
    
    // Repository (필요시)
    // single<CalendarRepository> { CalendarRepositoryImpl(get()) }
    
    // UseCase (필요시)
    // single { GetCalendarDataUseCase(get()) }
}
