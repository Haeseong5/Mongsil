package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.core.backup.BackupFileStore
import com.cashproject.mongsil.kmp.core.backup.DefaultDiaryBackupRepository
import com.cashproject.mongsil.kmp.core.backup.DiaryBackupRepository
import com.cashproject.mongsil.kmp.core.backup.PlatformBackupFileStore
import com.cashproject.mongsil.kmp.screen.setting.SettingViewModel
import com.cashproject.mongsil.kmp.screen.setting.backup.BackupRestoreViewModel
import com.cashproject.mongsil.kmp.screen.setting.backup.CreateBackupUseCase
import com.cashproject.mongsil.kmp.screen.setting.backup.RestoreBackupUseCase
import com.cashproject.mongsil.kmp.screen.setting.fontstyle.FontStyleViewModel
import com.cashproject.mongsil.kmp.screen.setting.pdfexport.PdfExportViewModel
import com.cashproject.mongsil.kmp.screen.setting.screenlock.AppLockViewModel
import com.cashproject.mongsil.kmp.screen.setting.screenlock.ScreenLockSettingsViewModel
import com.cashproject.mongsil.kmp.screen.setting.theme.ThemeSettingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val settingModule = module {
    single<BackupFileStore> { PlatformBackupFileStore() }
    single<DiaryBackupRepository> { DefaultDiaryBackupRepository(get(), get()) }
    single { CreateBackupUseCase(get()) }
    single { RestoreBackupUseCase(get()) }

    viewModel { SettingViewModel(get(), get(), get()) }
    viewModel { ThemeSettingViewModel(get()) }
    viewModel { FontStyleViewModel(get()) }
    viewModel { ScreenLockSettingsViewModel(get(), get()) }
    viewModel { AppLockViewModel(get()) }
    viewModel { PdfExportViewModel(get(), get(), get()) }
    viewModel { BackupRestoreViewModel(get(), get()) }
}
