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

package com.cashproject.mongsil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cashproject.mongsil.model.db.datasource.FirestoreDataSource
import com.cashproject.mongsil.model.db.datasource.LocalDataSource

/**
 * Factory for ViewModels
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val localDataSource: LocalDataSource, private val firestoreDataSource: FirestoreDataSource) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LockerViewModel::class.java) -> {
                LockerViewModel(localDataSource) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(localDataSource, firestoreDataSource) as T
            }
            modelClass.isAssignableFrom(CalendarViewModel::class.java) -> {
                CalendarViewModel(localDataSource, firestoreDataSource) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
