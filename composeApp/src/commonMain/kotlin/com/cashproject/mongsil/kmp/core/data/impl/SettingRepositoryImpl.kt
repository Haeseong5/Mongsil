package com.cashproject.mongsil.kmp.core.data.impl

import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.core.datastore.SettingsPreferenceDataSource
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow

class SettingRepositoryImpl(
    private val preferenceDataSource: SettingsPreferenceDataSource
) : SettingRepository {
    override fun isDarkTheme(): Flow<Boolean> = preferenceDataSource.isDarkTheme
    override fun themeMode(): Flow<ThemeMode> = preferenceDataSource.themeMode

    override suspend fun updateIsDarkTheme(isDarkTheme: Boolean) {
        preferenceDataSource.updateIsDarkTheme(isDarkTheme)
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        preferenceDataSource.updateThemeMode(themeMode)
    }
}
