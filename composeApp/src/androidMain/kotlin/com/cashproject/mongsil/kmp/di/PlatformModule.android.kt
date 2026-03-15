package com.cashproject.mongsil.kmp.di

import android.content.Context
import androidx.room.Room
import com.cashproject.mongsil.kmp.core.data.datasource.CounterLocalDataSource
import com.cashproject.mongsil.kmp.core.data.datasource.DiaryLocalDataSource
import com.cashproject.mongsil.kmp.core.data.datasource.impl.CounterLocalDataSourceRoom
import com.cashproject.mongsil.kmp.core.data.datasource.impl.DiaryLocalDataSourceRoom
import com.cashproject.mongsil.kmp.core.datastore.LocalPreferences
import com.cashproject.mongsil.kmp.core.datastore.LocalPreferencesImpl
import com.cashproject.mongsil.kmp.database.DatabaseDriverFactory
import com.cashproject.mongsil.kmp.database.MongsilRoomDatabase
import com.cashproject.mongsil.kmp.screen.setting.AndroidDiaryReminderScheduler
import com.cashproject.mongsil.kmp.screen.setting.DiaryReminderScheduler
import com.cashproject.mongsil.kmp.screen.setting.pdfexport.AndroidPdfExportService
import com.cashproject.mongsil.kmp.screen.setting.pdfexport.PdfExportService
import com.cashproject.mongsil.kmp.firebase.FirebaseService
import com.cashproject.mongsil.kmp.firebase.FirebaseServiceImpl
import com.cashproject.mongsil.kmp.screen.setting.screenlock.AndroidNativeScreenLockAuthenticator
import com.cashproject.mongsil.kmp.screen.setting.screenlock.CurrentActivityHolder
import com.cashproject.mongsil.kmp.screen.setting.screenlock.NativeScreenLockAuthenticator
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

    // ── DataSource 구현체 선택 (한 줄 교체로 SQLDelight ↔ Room 전환) ──
    single<DiaryLocalDataSource> { DiaryLocalDataSourceRoom(get()) }
    single<CounterLocalDataSource> { CounterLocalDataSourceRoom(get()) }

    // SQLDelight 사용 시 위 두 줄을 아래로 교체:
    // single<DiaryLocalDataSource> { DiaryLocalDataSourceSQLDelight(get()) }
    // single<CounterLocalDataSource> { CounterLocalDataSourceSQLDelight(get()) }
    // ─────────────────────────────────────────────────────────────────────

    factory<LocalPreferences> { params ->
        LocalPreferencesImpl(name = params.get())
    }

    single<DiaryReminderScheduler> { AndroidDiaryReminderScheduler(get()) }
    single<PdfExportService> { AndroidPdfExportService(get()) }
    single { CurrentActivityHolder() }
    single<NativeScreenLockAuthenticator> { AndroidNativeScreenLockAuthenticator(get()) }
    single<FirebaseService> { FirebaseServiceImpl() }
}
