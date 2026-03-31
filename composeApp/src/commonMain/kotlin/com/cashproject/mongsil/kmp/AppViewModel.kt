package com.cashproject.mongsil.kmp

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.migration.LegacyDataMigrator
import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    settingRepository: SettingRepository,
    private val legacyDataMigrator: LegacyDataMigrator,
) : BaseViewModel() {

    private val _migrationState = MutableStateFlow(MigrationState.CHECKING)

    val uiState = combine(
        settingRepository.themeMode(),
        settingRepository.fontStyleOption(),
        settingRepository.fontScale(),
        _migrationState,
    ) { themeMode, fontStyleOption, fontScale, migrationState ->
        AppUiState(
            themeMode = themeMode,
            fontStyleOption = fontStyleOption,
            fontScale = fontScale,
            migrationState = migrationState,
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
    }

    private fun checkAndMigrate() {
        viewModelScope.launch(exceptionHandler) {
            if (!legacyDataMigrator.needsMigration()) {
                _migrationState.value = MigrationState.DONE
                return@launch
            }

            _migrationState.value = MigrationState.MIGRATING
            legacyDataMigrator.migrate()
            _migrationState.value = MigrationState.DONE
        }
    }
}
