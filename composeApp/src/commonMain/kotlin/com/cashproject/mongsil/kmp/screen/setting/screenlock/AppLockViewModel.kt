package com.cashproject.mongsil.kmp.screen.setting.screenlock

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.model.ScreenLockMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

data class AppLockUiState(
    val shouldShowLockScreen: Boolean = false,
    val isEnabled: Boolean = false,
    val method: ScreenLockMethod = ScreenLockMethod.NONE,
    val passwordHash: String? = null,
)

class AppLockViewModel(
    settingRepository: SettingRepository,
) : BaseViewModel() {

    private val shouldLock = MutableStateFlow(false)
    private var hasInitialized = false
    private var didEnterBackground = false

    private val settingsState = combine(
        settingRepository.isScreenLockEnabled(),
        settingRepository.screenLockMethod(),
        settingRepository.screenLockPasswordHash(),
    ) { enabled, method, passwordHash ->
        AppLockUiState(
            shouldShowLockScreen = false,
            isEnabled = enabled && method != ScreenLockMethod.NONE,
            method = method,
            passwordHash = passwordHash,
        )
    }.onEach { settings ->
        when {
            !settings.isEnabled -> shouldLock.value = false
            !hasInitialized -> {
                hasInitialized = true
                shouldLock.value = true
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AppLockUiState(),
    )

    val uiState: StateFlow<AppLockUiState> = combine(
        settingsState,
        shouldLock,
    ) { settings, isLocked ->
        settings.copy(shouldShowLockScreen = settings.isEnabled && isLocked)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AppLockUiState(),
    )

    fun onAppBackgrounded() {
        if (uiState.value.isEnabled) {
            didEnterBackground = true
        }
    }

    fun onAppForegrounded() {
        if (uiState.value.isEnabled && didEnterBackground) {
            shouldLock.value = true
            didEnterBackground = false
        }
    }

    fun unlock() {
        shouldLock.value = false
        didEnterBackground = false
    }
}
