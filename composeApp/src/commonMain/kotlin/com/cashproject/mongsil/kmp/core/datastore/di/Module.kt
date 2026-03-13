package com.cashproject.mongsil.kmp.core.datastore.di

import com.cashproject.mongsil.kmp.core.datastore.SETTINGS_PREFERENCES_NAME
import com.cashproject.mongsil.kmp.core.datastore.SettingsPreferenceDataSource
import com.cashproject.mongsil.kmp.core.datastore.impl.DefaultSettingsPreferenceDataSource
import org.koin.core.parameter.parameterSetOf
import org.koin.dsl.module

// LocalPreferences factory는 각 플랫폼 모듈(PlatformModule)에서 등록됨
val datastoreSettingsModule = module {
    single<SettingsPreferenceDataSource> {
        DefaultSettingsPreferenceDataSource(
            localPreferences = get { parameterSetOf(SETTINGS_PREFERENCES_NAME) }
        )
    }

}
