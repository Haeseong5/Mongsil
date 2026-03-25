package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.screen.calendar.CalendarViewModel
import com.cashproject.mongsil.kmp.screen.diarychart.DiaryChartViewModel
import com.cashproject.mongsil.kmp.screen.diarychart.GetWordCloudUseCase
import com.cashproject.mongsil.kmp.screen.diarymonthly.DiaryListViewModel
import com.cashproject.mongsil.kmp.screen.diarysearch.DiarySearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 캘린더 Feature 모듈
 * 캘린더 관련 ViewModel, Repository, UseCase 등록
 */
internal val calendarModule = module {
    factory { GetWordCloudUseCase(diaryRepository = get<DiaryRepository>()) }

    viewModel {
        CalendarViewModel(
            diaryRepository = get<DiaryRepository>(),
            emoticonRepository = get<EmoticonRepository>()
        )
    }

    viewModel {
        DiarySearchViewModel(
            diaryRepository = get<DiaryRepository>(),
            emoticonRepository = get<EmoticonRepository>()
        )
    }

    viewModel { (year: Int, month: Int) ->
        DiaryChartViewModel(
            diaryRepository = get<DiaryRepository>(),
            emoticonRepository = get<EmoticonRepository>(),
            getWordCloudUseCase = get<GetWordCloudUseCase>(),
            initialYear = year,
            initialMonth = month
        )
    }

    viewModel { (year: Int, month: Int) ->
        DiaryListViewModel(
            diaryRepository = get<DiaryRepository>(),
            emoticonRepository = get<EmoticonRepository>(),
            initialYear = year,
            initialMonth = month
        )
    }
}
