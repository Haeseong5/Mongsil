package com.cashproject.mongsil.kmp.core.datastore

import kotlinx.coroutines.flow.Flow

interface SettingsPreferenceDataSource {

    val isDarkTheme: Flow<Boolean>

    suspend fun updateIsDarkTheme(isDarkTheme: Boolean)
}