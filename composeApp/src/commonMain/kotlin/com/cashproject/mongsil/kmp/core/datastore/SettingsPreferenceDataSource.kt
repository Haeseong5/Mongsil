package com.cashproject.mongsil.kmp.core.datastore

import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ScreenLockMethod
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsPreferenceDataSource {

    val isDarkTheme: Flow<Boolean>
    val themeMode: Flow<ThemeMode>
    val fontStyleOption: Flow<FontStyleOption>
    val fontScale: Flow<Float>

    fun getThemeModeSync(): ThemeMode
    fun getFontStyleOptionSync(): FontStyleOption
    fun getFontScaleSync(): Float
    val isDiaryReminderEnabled: Flow<Boolean>
    val isScreenLockEnabled: Flow<Boolean>
    val screenLockMethod: Flow<ScreenLockMethod>
    val screenLockPasswordHash: Flow<String?>

    suspend fun updateIsDarkTheme(isDarkTheme: Boolean)
    suspend fun updateThemeMode(themeMode: ThemeMode)
    suspend fun updateFontStyleOption(option: FontStyleOption)
    suspend fun updateFontScale(scale: Float)
    suspend fun updateDiaryReminderEnabled(enabled: Boolean)
    suspend fun updateScreenLockEnabled(enabled: Boolean)
    suspend fun updateScreenLockMethod(method: ScreenLockMethod)
    suspend fun updateScreenLockPasswordHash(passwordHash: String?)
}
