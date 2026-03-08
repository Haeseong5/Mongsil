package com.cashproject.mongsil.kmp.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.cashproject.mongsil.kmp.core.datastore.LocalPreferences
import com.cashproject.mongsil.kmp.core.datastore.LocalPreferencesImpl
import com.cashproject.mongsil.kmp.database.DatabaseDriverFactory
import com.cashproject.mongsil.kmp.database.MongsilRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * iOS 플랫폼 의존성 모듈
 */
actual fun platformModule(): Module = module {
    // SQLDelight Driver
    single { DatabaseDriverFactory() }

    // Room Database
    single<MongsilRoomDatabase> {
        Room.databaseBuilder<MongsilRoomDatabase>(
            name = documentDirectory() + "/mongsil_v2.db",
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    factory<LocalPreferences> { params ->
        LocalPreferencesImpl(name = params.get())
    }
}

private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
