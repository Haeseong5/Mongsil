package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.screen.diarywrite.DiaryWriteViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 일기 Feature 모듈
 * 일기 작성 관련 ViewModel 등록
 */
internal val diaryModule = module {
    // ViewModel with parameters
    viewModel { (year: Int, month: Int, day: Int) ->
        DiaryWriteViewModel(
            diaryRepository = get(),
            year = year,
            month = month,
            day = day
        )
    }
}
