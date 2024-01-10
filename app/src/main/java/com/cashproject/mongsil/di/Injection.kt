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
import com.cashproject.mongsil.data.db.AppDatabase
import com.cashproject.mongsil.data.db.dao.BookmarkDao
import com.cashproject.mongsil.data.db.dao.DiaryDao
import com.cashproject.mongsil.data.firebase.FireStoreDataSource
import com.cashproject.mongsil.data.service.BookmarkService
import com.cashproject.mongsil.data.service.DiaryService
import com.cashproject.mongsil.data.service.PosterService
import com.cashproject.mongsil.repository.BookmarkRepository
import com.cashproject.mongsil.repository.PosterRepository
import com.cashproject.mongsil.viewmodel.ViewModelFactory


/**
 * Enables injection of data sources.
 */
object Injection {

    /** local */
    private fun provideBookmarkDao(context: Context): BookmarkDao {
        val database = AppDatabase.getInstance(context)
        return database.bookmarkDao()
    }

    private fun provideDiaryDao(context: Context): DiaryDao {
        val database = AppDatabase.getInstance(context)
        return database.diaryDao()
    }

    /** remote */
    private fun providePosterService(): PosterService {
        return PosterService
    }

    private fun provideFirestoreDataSource(): FireStoreDataSource {
        return FireStoreDataSource()
    }

    /** repository */
    fun provideMemoryCacheRepository(posterService: PosterService): PosterRepository {
        return PosterRepository(posterService)
    }

    private fun provideBookmarkRepository(
        bookmarkService: BookmarkService,
        posterRepository: PosterRepository,
    ): BookmarkRepository {
        return BookmarkRepository(
            bookmarkService = bookmarkService,
            posterRepository = posterRepository
        )
    }

    /** viewModel */
    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val commentDataSource = provideDiaryDao(context)

        val diaryService = DiaryService(commentDataSource)
        return ViewModelFactory(diaryService)
    }
}
