package com.cashproject.mongsil.kmp.core.data.di

import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.core.data.impl.SettingRepositoryImpl
import org.koin.dsl.module

val coreDataSettingModule = module {
    single<SettingRepository> { SettingRepositoryImpl(get()) }
}
