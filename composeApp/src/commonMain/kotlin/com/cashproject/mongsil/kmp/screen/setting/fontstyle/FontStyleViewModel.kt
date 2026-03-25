package com.cashproject.mongsil.kmp.screen.setting.fontstyle

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.model.FontStyleOption
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FontStyleViewModel(
    private val settingRepository: SettingRepository,
) : BaseViewModel() {

    val selectedFontStyle = settingRepository
        .fontStyleOption()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            FontStyleOption.GAMJA_FLOWER
        )

    val fontScale = settingRepository
        .fontScale()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DEFAULT_FONT_SCALE
        )

    fun updateFontStyle(option: FontStyleOption) {
        viewModelScope.launch(exceptionHandler) {
            settingRepository.updateFontStyleOption(option)
        }
    }

    fun updateFontScale(scale: Float) {
        viewModelScope.launch(exceptionHandler) {
            settingRepository.updateFontScale(scale.coerceIn(MIN_FONT_SCALE, MAX_FONT_SCALE))
        }
    }

    companion object {
        const val MIN_FONT_SCALE = 0.8f
        const val MAX_FONT_SCALE = 1.4f
        const val DEFAULT_FONT_SCALE = 1f
    }
}
