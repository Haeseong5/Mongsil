/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cashproject.mongsil.di

import android.content.Context
import com.cashproject.mongsil.model.db.AppDatabase
import com.cashproject.mongsil.model.db.LockerDao
import com.cashproject.mongsil.viewmodel.ViewModelFactory


/**
 * Enables injection of data sources.
 */
object Injection {

    fun provideLocalDataSource(context: Context): LockerDao {
        val database = AppDatabase.getInstance(context)
        return database.lockerDao()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideLocalDataSource(context)
        return ViewModelFactory(dataSource)
    }
}
