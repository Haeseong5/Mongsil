package com.cashproject.mongsil.kmp.core.datastore.impl

import com.cashproject.mongsil.kmp.core.datastore.LocalPreferences
import com.cashproject.mongsil.kmp.core.datastore.SettingsPreferenceDataSource
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultSettingsPreferenceDataSource(
    private val localPreferences: LocalPreferences,
) : SettingsPreferenceDataSource {

    override val isDarkTheme: Flow<Boolean> = localPreferences
        .getBoolean(KEY_IS_DARK_THEME)
        .map { it ?: false }

    override val themeMode: Flow<ThemeMode> = localPreferences
        .getString(KEY_THEME_MODE)
        .map { ThemeMode.fromKey(it ?: ThemeMode.SYSTEM.key) }

    override suspend fun updateIsDarkTheme(isDarkTheme: Boolean) {
        localPreferences.setBoolean(KEY_IS_DARK_THEME, isDarkTheme)
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        localPreferences.setString(KEY_THEME_MODE, themeMode.key)
    }

    companion object {
        private const val KEY_IS_DARK_THEME = "IS_DARK_THEME"
        private const val KEY_THEME_MODE = "THEME_MODE"
    }
}
