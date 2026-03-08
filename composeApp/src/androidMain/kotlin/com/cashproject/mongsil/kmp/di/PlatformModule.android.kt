package com.cashproject.mongsil.kmp.di

import android.content.Context
import androidx.room.Room
import com.cashproject.mongsil.kmp.core.datastore.LocalPreferences
import com.cashproject.mongsil.kmp.core.datastore.LocalPreferencesImpl
import com.cashproject.mongsil.kmp.database.DatabaseDriverFactory
import com.cashproject.mongsil.kmp.database.MongsilRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android 플랫폼 의존성 모듈
 */
actual fun platformModule(): Module = module {
    // SQLDelight Driver
    single { DatabaseDriverFactory(get<Context>()) }

    // Room Database
    single<MongsilRoomDatabase> {
        Room.databaseBuilder<MongsilRoomDatabase>(
            context = get<Context>().applicationContext,
            name = "mongsil_v2.db",
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    factory<LocalPreferences> { params ->
        LocalPreferencesImpl(name = params.get())
    }
}
