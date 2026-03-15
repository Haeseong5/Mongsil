package com.cashproject.mongsil.kmp.core.data.impl

import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.core.datastore.SettingsPreferenceDataSource
import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ScreenLockMethod
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow

class SettingRepositoryImpl(
    private val preferenceDataSource: SettingsPreferenceDataSource
) : SettingRepository {
    override fun isDarkTheme(): Flow<Boolean> = preferenceDataSource.isDarkTheme
    override fun themeMode(): Flow<ThemeMode> = preferenceDataSource.themeMode
    override fun fontStyleOption(): Flow<FontStyleOption> = preferenceDataSource.fontStyleOption
    override fun fontScale(): Flow<Float> = preferenceDataSource.fontScale
    override fun isDiaryReminderEnabled(): Flow<Boolean> = preferenceDataSource.isDiaryReminderEnabled
    override fun isScreenLockEnabled(): Flow<Boolean> = preferenceDataSource.isScreenLockEnabled
    override fun screenLockMethod(): Flow<ScreenLockMethod> = preferenceDataSource.screenLockMethod
    override fun screenLockPasswordHash(): Flow<String?> = preferenceDataSource.screenLockPasswordHash

    override fun getThemeModeSync(): ThemeMode = preferenceDataSource.getThemeModeSync()

    override fun getFontStyleOptionSync(): FontStyleOption = preferenceDataSource.getFontStyleOptionSync()

    override fun getFontScaleSync(): Float = preferenceDataSource.getFontScaleSync()

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

    override suspend fun updateDiaryReminderEnabled(enabled: Boolean) {
        preferenceDataSource.updateDiaryReminderEnabled(enabled)
    }

    override suspend fun updateScreenLockEnabled(enabled: Boolean) {
        preferenceDataSource.updateScreenLockEnabled(enabled)
    }

    override suspend fun updateScreenLockMethod(method: ScreenLockMethod) {
        preferenceDataSource.updateScreenLockMethod(method)
    }

    override suspend fun updateScreenLockPasswordHash(passwordHash: String?) {
        preferenceDataSource.updateScreenLockPasswordHash(passwordHash)
    }
}
