package com.cashproject.mongsil.kmp

import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ThemeMode

data class AppUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val fontStyleOption: FontStyleOption = FontStyleOption.GAMJA_FLOWER,
    val fontScale: Float = 1f,
    val migrationState: MigrationState = MigrationState.CHECKING,
)

enum class MigrationState {
    CHECKING,
    MIGRATING,
    DONE,
}
