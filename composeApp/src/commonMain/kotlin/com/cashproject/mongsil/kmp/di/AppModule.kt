package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.AppViewModel
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.datasource.CounterLocalDataSource
import com.cashproject.mongsil.kmp.core.data.datasource.DiaryLocalDataSource
import com.cashproject.mongsil.kmp.core.data.datasource.impl.CounterLocalDataSourceRoom
import com.cashproject.mongsil.kmp.core.data.datasource.impl.CounterLocalDataSourceSQLDelight
import com.cashproject.mongsil.kmp.core.data.datasource.impl.DiaryLocalDataSourceRoom
import com.cashproject.mongsil.kmp.core.data.datasource.impl.DiaryLocalDataSourceSQLDelight
import com.cashproject.mongsil.kmp.core.data.di.coreDataSettingModule
import com.cashproject.mongsil.kmp.core.datastore.di.datastoreSettingsModule
import com.cashproject.mongsil.kmp.database.DatabaseDriverFactory
import com.cashproject.mongsil.kmp.database.MongsilDatabase
import com.cashproject.mongsil.kmp.database.MongsilRoomDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 플랫폼별 모듈 (expect/actual)
 * Android: DatabaseDriverFactory, MongsilRoomDatabase, 기타 Android 의존성
 * iOS: DatabaseDriverFactory, MongsilRoomDatabase, 기타 iOS 의존성
 */
expect fun platformModule(): Module

/**
 * Database 모듈
 *
 * ✅ 현재 사용 중인 구현체를 변경하려면 아래 DataSource 바인딩 한 줄만 교체하면 됩니다.
 *
 * Room  → DiaryLocalDataSourceRoom(get()),  CounterLocalDataSourceRoom(get())
 * SQLDelight → DiaryLocalDataSourceSQLDelight(get()), CounterLocalDataSourceSQLDelight(get())
 */
internal val databaseModule = module {
    // SQLDelight Database
    single { MongsilDatabase(get<DatabaseDriverFactory>().createDriver()) }

    // Room DAOs
    single { get<MongsilRoomDatabase>().diaryDao() }
    single { get<MongsilRoomDatabase>().counterDao() }
    single { get<MongsilRoomDatabase>().emoticonDao() }

    // ── DataSource 구현체 선택 (한 줄 교체로 SQLDelight ↔ Room 전환) ──────────
    single<DiaryLocalDataSource> { DiaryLocalDataSourceRoom(get()) }
    single<CounterLocalDataSource> { CounterLocalDataSourceRoom(get()) }

    // SQLDelight 사용 시 위 두 줄을 아래로 교체:
    // single<DiaryLocalDataSource> { DiaryLocalDataSourceSQLDelight(get()) }
    // single<CounterLocalDataSource> { CounterLocalDataSourceSQLDelight(get()) }
    // ─────────────────────────────────────────────────────────────────────────
}

/**
 * Repository 전용 모듈
 */
internal val repositoryModule = module {
    single { DiaryRepository(get()) }
}

/**
 * App-level ViewModel 모듈
 */
internal val appViewModelModule = module {
    viewModel { AppViewModel(get()) }
}

/**
 * 앱 전체 모듈 통합
 */
val appModules: List<Module> = listOf(
    platformModule(),
    databaseModule,
    networkModule,
    repositoryModule,
    calendarModule,
    counterModule,
    diaryModule,
    datastoreSettingsModule,
    coreDataSettingModule,
    appViewModelModule,
    settingModule,
)
