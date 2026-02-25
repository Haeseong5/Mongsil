package com.cashproject.mongsil.kmp.core.datastore

import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsPreferenceDataSource {

    val isDarkTheme: Flow<Boolean>
    val themeMode: Flow<ThemeMode>

    suspend fun updateIsDarkTheme(isDarkTheme: Boolean)
    suspend fun updateThemeMode(themeMode: ThemeMode)
}
