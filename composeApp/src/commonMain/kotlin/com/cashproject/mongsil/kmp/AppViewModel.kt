package com.cashproject.mongsil.kmp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AppViewModel(
    settingRepository: SettingRepository,
) : ViewModel() {

    val uiState = combine(
        settingRepository.themeMode(),
        settingRepository.fontStyleOption(),
        settingRepository.fontScale()
    ) { themeMode, fontStyleOption, fontScale ->
        AppUiState(
            themeMode = themeMode,
            fontStyleOption = fontStyleOption,
            fontScale = fontScale
        )
    }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppUiState(
                themeMode = settingRepository.getThemeModeSync(),
                fontStyleOption = settingRepository.getFontStyleOptionSync(),
                fontScale = settingRepository.getFontScaleSync(),
            )
        )
}
