package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.AppViewModel
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
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
 * Database 모듈 — DB 인스턴스 및 DAO 제공
 * DataSource 바인딩은 각 platformModule()에서 담당합니다.
 *
 * ✅ 구현체 전환 방법: 각 플랫폼의 PlatformModule.X.kt 에서 한 줄 교체
 *   Room       → DiaryLocalDataSourceRoom(get())
 *   SQLDelight → DiaryLocalDataSourceSQLDelight(get())
 */
internal val databaseModule = module {
    // SQLDelight Database (lazy — 플랫폼이 SQLDelight DataSource를 선택한 경우에만 초기화됨)
    single { MongsilDatabase(get<DatabaseDriverFactory>().createDriver()) }

    // Room DAOs (lazy — 플랫폼이 Room DataSource를 선택한 경우에만 초기화됨)
    single { get<MongsilRoomDatabase>().diaryDao() }
    single { get<MongsilRoomDatabase>().counterDao() }
    single { get<MongsilRoomDatabase>().emoticonDao() }
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
    viewModel { AppViewModel(get(), get(), get()) }
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
    diaryModule,
    datastoreSettingsModule,
    coreDataSettingModule,
    appViewModelModule,
    settingModule,
)
