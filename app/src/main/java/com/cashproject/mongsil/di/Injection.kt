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
import com.cashproject.mongsil.model.db.dao.CommentDao
import com.cashproject.mongsil.model.db.dao.LockerDao
import com.cashproject.mongsil.model.db.datasource.FirestoreDataSource
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import com.cashproject.mongsil.viewmodel.ViewModelFactory


/**
 * Enables injection of data sources.
 */
object Injection {

    private fun provideLocalDataSource(context: Context): LockerDao {
        val database = AppDatabase.getInstance(context)
        return database.lockerDao()
    }

    private fun provideCommentDataSource(context: Context): CommentDao {
        val database = AppDatabase.getInstance(context)
        return database.commentDao()
    }

    private fun provideFirestoreDataSource(): FirestoreDataSource {
        return FirestoreDataSource()
    }


    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val lockerDataSource = provideLocalDataSource(context)
        val commentDataSource = provideCommentDataSource(context)

        val localDataSource = LocalDataSource(commentDataSource, lockerDataSource)
        return ViewModelFactory(localDataSource, provideFirestoreDataSource())
    }
}
