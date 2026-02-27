package com.cashproject.mongsil.kmp.core.datastore.impl

import com.cashproject.mongsil.kmp.core.datastore.LocalPreferences
import com.cashproject.mongsil.kmp.core.datastore.SettingsPreferenceDataSource
import com.cashproject.mongsil.kmp.model.FontStyleOption
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

    override val fontStyleOption: Flow<FontStyleOption> = localPreferences
        .getString(KEY_FONT_STYLE_OPTION)
        .map { FontStyleOption.fromKey(it ?: FontStyleOption.GAMJA_FLOWER.key) }

    override val fontScale: Flow<Float> = localPreferences
        .getFloat(KEY_FONT_SCALE)
        .map { (it ?: DEFAULT_FONT_SCALE).coerceIn(MIN_FONT_SCALE, MAX_FONT_SCALE) }

    override suspend fun updateIsDarkTheme(isDarkTheme: Boolean) {
        localPreferences.setBoolean(KEY_IS_DARK_THEME, isDarkTheme)
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        localPreferences.setString(KEY_THEME_MODE, themeMode.key)
    }

    override suspend fun updateFontStyleOption(option: FontStyleOption) {
        localPreferences.setString(KEY_FONT_STYLE_OPTION, option.key)
    }

    override suspend fun updateFontScale(scale: Float) {
        localPreferences.setFloat(
            KEY_FONT_SCALE,
            scale.coerceIn(MIN_FONT_SCALE, MAX_FONT_SCALE)
        )
    }

    companion object {
        private const val KEY_IS_DARK_THEME = "IS_DARK_THEME"
        private const val KEY_THEME_MODE = "THEME_MODE"
        private const val KEY_FONT_STYLE_OPTION = "FONT_STYLE_OPTION"
        private const val KEY_FONT_SCALE = "FONT_SCALE"
        private const val DEFAULT_FONT_SCALE = 1f
        private const val MIN_FONT_SCALE = 0.8f
        private const val MAX_FONT_SCALE = 1.4f
    }
}
