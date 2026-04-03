package com.cashproject.mongsil.kmp

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.firebase.AppUpdateChecker
import com.cashproject.mongsil.kmp.migration.LegacyDataMigrator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    settingRepository: SettingRepository,
    private val legacyDataMigrator: LegacyDataMigrator,
    private val appUpdateChecker: AppUpdateChecker,
) : BaseViewModel() {

    private val _migrationState = MutableStateFlow(needsMigration())
    private val _updateInfo = MutableStateFlow<UpdateInfo?>(null)

    val uiState = combine(
        settingRepository.themeMode(),
        settingRepository.fontStyleOption(),
        settingRepository.fontScale(),
        _migrationState,
        _updateInfo,
    ) { themeMode, fontStyleOption, fontScale, migrationState, updateInfo ->
        AppUiState(
            themeMode = themeMode,
            fontStyleOption = fontStyleOption,
            fontScale = fontScale,
            migrationState = migrationState,
            updateInfo = updateInfo,
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

    init {
        checkAndMigrate()
        checkAppUpdate()
    }

    private fun needsMigration(): MigrationState {
        return if (!legacyDataMigrator.needsMigration()) {
            MigrationState.DONE
        } else {
            MigrationState.MIGRATING
        }
    }

    private fun checkAndMigrate() {
        viewModelScope.launch(exceptionHandler) {
            if (!legacyDataMigrator.needsMigration()) return@launch
            legacyDataMigrator.migrate()
            _migrationState.value = MigrationState.DONE
        }
    }

    private fun checkAppUpdate() {
        viewModelScope.launch(exceptionHandler) {
            val versionInfo = appUpdateChecker.fetchLatestVersion() ?: return@launch
            val currentCode = appUpdateChecker.getCurrentVersionCode()
            if (currentCode < versionInfo.latestVersionCode) {
                _updateInfo.value = UpdateInfo(
                    currentVersion = appUpdateChecker.getCurrentVersionName(),
                    latestVersion = versionInfo.latestVersionName,
                )
            }
        }
    }

    fun dismissUpdateDialog() {
        _updateInfo.value = null
    }
}
