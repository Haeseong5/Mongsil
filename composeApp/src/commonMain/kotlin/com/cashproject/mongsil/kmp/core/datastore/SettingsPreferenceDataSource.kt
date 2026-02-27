package com.cashproject.mongsil.kmp.core.datastore

import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsPreferenceDataSource {

    val isDarkTheme: Flow<Boolean>
    val themeMode: Flow<ThemeMode>
    val fontStyleOption: Flow<FontStyleOption>
    val fontScale: Flow<Float>

    suspend fun updateIsDarkTheme(isDarkTheme: Boolean)
    suspend fun updateThemeMode(themeMode: ThemeMode)
    suspend fun updateFontStyleOption(option: FontStyleOption)
    suspend fun updateFontScale(scale: Float)
}
