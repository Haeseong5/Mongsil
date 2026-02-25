package com.cashproject.mongsil.kmp.core.data.impl

import com.cashproject.mongsil.kmp.core.datastore.SettingsPreferenceDataSource
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import kotlinx.coroutines.flow.Flow

class SettingRepositoryImpl(
    private val preferenceDataSource: SettingsPreferenceDataSource
) : SettingRepository {
    override fun isDarkTheme(): Flow<Boolean> = preferenceDataSource.isDarkTheme

    override suspend fun updateIsDarkTheme(isDarkTheme: Boolean) {
        preferenceDataSource.updateIsDarkTheme(isDarkTheme)
    }
}