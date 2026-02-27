package com.cashproject.mongsil.kmp.core.data

import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun isDarkTheme(): Flow<Boolean>
    fun themeMode(): Flow<ThemeMode>
    fun fontStyleOption(): Flow<FontStyleOption>
    fun fontScale(): Flow<Float>

    suspend fun updateIsDarkTheme(isDarkTheme: Boolean)
    suspend fun updateThemeMode(themeMode: ThemeMode)
    suspend fun updateFontStyleOption(option: FontStyleOption)
    suspend fun updateFontScale(scale: Float)
}
