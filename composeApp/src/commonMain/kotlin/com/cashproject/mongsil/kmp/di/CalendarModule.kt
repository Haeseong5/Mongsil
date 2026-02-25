package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.screen.calendar.CalendarViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 캘린더 Feature 모듈
 * 캘린더 관련 ViewModel, Repository, UseCase 등록
 */
internal val calendarModule = module {
    // ViewModel - 명시적으로 타입 지정
    viewModel { 
        CalendarViewModel(
            diaryRepository = get<DiaryRepository>(),
            emoticonRepository = get<EmoticonRepository>()
        )
    }
}
