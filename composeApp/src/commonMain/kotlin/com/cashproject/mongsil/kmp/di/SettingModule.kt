package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.core.backup.BackupRepository
import com.cashproject.mongsil.kmp.core.backup.BackupSerializer
import com.cashproject.mongsil.kmp.core.backup.DefaultBackupRepository
import com.cashproject.mongsil.kmp.screen.setting.SettingViewModel
import com.cashproject.mongsil.kmp.screen.setting.backup.BackupRestoreViewModel
import com.cashproject.mongsil.kmp.screen.setting.backup.CreateBackupUseCase
import com.cashproject.mongsil.kmp.screen.setting.backup.RestoreBackupUseCase
import com.cashproject.mongsil.kmp.screen.setting.backup.ValidateBackupUseCase
import com.cashproject.mongsil.kmp.screen.setting.fontstyle.FontStyleViewModel
import com.cashproject.mongsil.kmp.screen.setting.pdfexport.PdfExportViewModel
import com.cashproject.mongsil.kmp.screen.setting.screenlock.AppLockViewModel
import com.cashproject.mongsil.kmp.screen.setting.screenlock.ScreenLockSettingsViewModel
import com.cashproject.mongsil.kmp.screen.setting.theme.ThemeSettingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val settingModule = module {
    viewModel { SettingViewModel(get(), get(), get()) }
    viewModel { ThemeSettingViewModel(get()) }
    viewModel { FontStyleViewModel(get()) }
    viewModel { ScreenLockSettingsViewModel(get(), get()) }
    viewModel { AppLockViewModel(get()) }
    viewModel { PdfExportViewModel(get(), get(), get()) }

    single { BackupSerializer() }
    single<BackupRepository> { DefaultBackupRepository(get()) }
    single { CreateBackupUseCase(get(), get()) }
    single { RestoreBackupUseCase(get(), get()) }
    single { ValidateBackupUseCase(get()) }
    viewModel { BackupRestoreViewModel(get(), get(), get(), getOrNull()) }
}
