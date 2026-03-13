package com.cashproject.mongsil.kmp.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository,
    private val diaryReminderScheduler: DiaryReminderScheduler,
) : ViewModel() {

    val isDiaryReminderEnabled = settingRepository
        .isDiaryReminderEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun updateDiaryReminder(enabled: Boolean) {
        viewModelScope.launch {
            val appliedEnabled = diaryReminderScheduler.setEnabled(enabled)
            settingRepository.updateDiaryReminderEnabled(appliedEnabled)
        }
    }
}
