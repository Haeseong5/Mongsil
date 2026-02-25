package com.cashproject.mongsil.kmp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppViewModel(
    settingRepository: SettingRepository,
) : ViewModel() {

    val uiState = settingRepository
        .isDarkTheme()
        .map { AppUiState(isDarkTheme = it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppUiState(isDarkTheme = false)
        )
}

