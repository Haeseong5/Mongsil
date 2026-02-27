package com.cashproject.mongsil.kmp.core.data.impl

import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.core.datastore.SettingsPreferenceDataSource
import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow

class SettingRepositoryImpl(
    private val preferenceDataSource: SettingsPreferenceDataSource
) : SettingRepository {
    override fun isDarkTheme(): Flow<Boolean> = preferenceDataSource.isDarkTheme
    override fun themeMode(): Flow<ThemeMode> = preferenceDataSource.themeMode
    override fun fontStyleOption(): Flow<FontStyleOption> = preferenceDataSource.fontStyleOption
    override fun fontScale(): Flow<Float> = preferenceDataSource.fontScale

    override suspend fun updateIsDarkTheme(isDarkTheme: Boolean) {
        preferenceDataSource.updateIsDarkTheme(isDarkTheme)
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        preferenceDataSource.updateThemeMode(themeMode)
    }

    override suspend fun updateFontStyleOption(option: FontStyleOption) {
        preferenceDataSource.updateFontStyleOption(option)
    }

    override suspend fun updateFontScale(scale: Float) {
        preferenceDataSource.updateFontScale(scale)
    }
}
