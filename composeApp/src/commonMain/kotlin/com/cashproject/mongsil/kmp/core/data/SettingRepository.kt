package com.cashproject.mongsil.kmp.core.data

import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun isDarkTheme(): Flow<Boolean>
    fun themeMode(): Flow<ThemeMode>

    suspend fun updateIsDarkTheme(isDarkTheme: Boolean)
    suspend fun updateThemeMode(themeMode: ThemeMode)
}
