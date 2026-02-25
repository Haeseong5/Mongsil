package com.cashproject.mongsil.kmp.screen.setting.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeSettingViewModel(
    private val settingRepository: SettingRepository,
) : ViewModel() {

    val selectedMode = settingRepository
        .themeMode()
        .map { it }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ThemeMode.SYSTEM
        )

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingRepository.updateThemeMode(mode)
        }
    }
}
