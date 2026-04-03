package com.cashproject.mongsil.kmp

import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ThemeMode

data class AppUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val fontStyleOption: FontStyleOption = FontStyleOption.GAMJA_FLOWER,
    val fontScale: Float = 1f,
    val migrationState: MigrationState = MigrationState.IDLE,
    val updateInfo: UpdateInfo? = null,
)

data class UpdateInfo(
    val currentVersion: String,
    val latestVersion: String,
)

enum class MigrationState {
    IDLE,
    MIGRATING,
    DONE,
}
