package com.cashproject.mongsil.kmp.core.datastore.impl

import com.cashproject.mongsil.kmp.core.datastore.LocalPreferences
import com.cashproject.mongsil.kmp.core.datastore.SettingsPreferenceDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultSettingsPreferenceDataSource(
    private val localPreferences: LocalPreferences,
) : SettingsPreferenceDataSource {
    override val isDarkTheme: Flow<Boolean> = localPreferences
        .getBoolean(KEY_IS_DARK_THEME)
        .map { it ?: false }


    override suspend fun updateIsDarkTheme(isDarkTheme: Boolean) {
        localPreferences.setBoolean(KEY_IS_DARK_THEME, isDarkTheme)
    }

    companion object {
        private const val KEY_IS_DARK_THEME = "IS_DARK_THEME"

    }
}