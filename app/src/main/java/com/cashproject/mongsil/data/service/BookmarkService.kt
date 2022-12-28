package com.cashproject.mongsil.data.service

import com.cashproject.mongsil.data.db.AppDatabase
import com.cashproject.mongsil.data.db.dao.BookmarkDao
import com.cashproject.mongsil.data.db.entity.SayingEntity

class BookmarkService(
    private val bookmarkDao: BookmarkDao = AppDatabase.getInstance().bookmarkDao()
) {
    suspend fun getAllBookmarkedPosters(): List<SayingEntity> {
        return bookmarkDao.getAll()
    }

    suspend fun insertBookmarkPoster(id: String) {
        return bookmarkDao.insert(SayingEntity(docId = id))
    }

    suspend fun deleteBookmarkPoster(id: String) {
        return bookmarkDao.delete(id)
    }
}