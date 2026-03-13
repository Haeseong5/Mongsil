package com.cashproject.mongsil.kmp.screen.setting.screenlock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.model.ScreenLockMethod
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ScreenLockSettingsUiState(
    val isEnabled: Boolean = false,
    val method: ScreenLockMethod = ScreenLockMethod.NONE,
    val hasPassword: Boolean = false,
    val nativeAvailability: NativeScreenLockAvailability = NativeScreenLockAvailability(
        isAvailable = false,
        title = "기기 잠금 사용 불가",
        description = "이 기기에서는 시스템 잠금을 바로 사용할 수 없습니다.",
    ),
)

class ScreenLockSettingsViewModel(
    private val settingRepository: SettingRepository,
    private val nativeAuthenticator: NativeScreenLockAuthenticator,
) : ViewModel() {

    val uiState = combine(
        settingRepository.isScreenLockEnabled(),
        settingRepository.screenLockMethod(),
        settingRepository.screenLockPasswordHash(),
    ) { isEnabled, method, passwordHash ->
        ScreenLockSettingsUiState(
            isEnabled = isEnabled && method != ScreenLockMethod.NONE,
            method = method,
            hasPassword = !passwordHash.isNullOrBlank(),
            nativeAvailability = nativeAuthenticator.availability(),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ScreenLockSettingsUiState(),
    )

    fun updateNativeLockEnabled(enabled: Boolean) {
        if (!nativeAuthenticator.availability().isAvailable) return

        viewModelScope.launch {
            settingRepository.updateScreenLockMethod(
                if (enabled) ScreenLockMethod.SYSTEM else ScreenLockMethod.NONE
            )
            settingRepository.updateScreenLockEnabled(enabled)
        }
    }

    fun updateAppPassword(password: String) {
        val normalized = password.trim()
        if (normalized.length < MIN_PASSWORD_LENGTH) return

        viewModelScope.launch {
            settingRepository.updateScreenLockPasswordHash(PasswordHasher.hash(normalized))
            settingRepository.updateScreenLockMethod(ScreenLockMethod.APP_PASSWORD)
            settingRepository.updateScreenLockEnabled(true)
        }
    }

    fun updateAppPasswordEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val method = if (enabled) ScreenLockMethod.APP_PASSWORD else ScreenLockMethod.NONE
            settingRepository.updateScreenLockMethod(method)
            settingRepository.updateScreenLockEnabled(enabled)
        }
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 4
    }
}
