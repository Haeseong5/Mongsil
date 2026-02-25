package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.screen.setting.theme.ThemeSettingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val settingModule = module {
    viewModel { ThemeSettingViewModel(get()) }
}
