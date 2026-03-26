package com.cashproject.mongsil.kmp.screen.setting

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.firebase.FirebaseService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository,
    private val diaryReminderScheduler: DiaryReminderScheduler,
    private val firebaseService: FirebaseService,
) : BaseViewModel() {

    val isDiaryReminderEnabled = settingRepository
        .isDiaryReminderEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    val isEmoticonTranslucentEnabled = settingRepository
        .isEmoticonTranslucentEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun updateDiaryReminder(enabled: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            val appliedEnabled = diaryReminderScheduler.setEnabled(enabled)
            settingRepository.updateDiaryReminderEnabled(appliedEnabled)
        }
    }

    fun updateEmoticonTranslucent(enabled: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            settingRepository.updateEmoticonTranslucentEnabled(enabled)
        }
    }

    fun logMenuClick(menu: String) {
        firebaseService.logEvent(
            name = EVENT_SETTING_MENU_CLICKED,
            params = mapOf(PARAM_MENU to menu)
        )
    }

    private companion object {
        const val EVENT_SETTING_MENU_CLICKED = "setting_menu_clicked"
        const val PARAM_MENU = "menu"
    }
}
