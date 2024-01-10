package com.cashproject.mongsil.data.service

import com.cashproject.mongsil.data.db.AppDatabase
import com.cashproject.mongsil.data.db.dao.BookmarkDao
import com.cashproject.mongsil.data.db.entity.SayingEntity
import kotlinx.coroutines.flow.Flow

class BookmarkService(
    private val bookmarkDao: BookmarkDao = AppDatabase.getInstance().bookmarkDao()
) {

    suspend fun findPosterById(id: String): SayingEntity? {
        return bookmarkDao.findByDocId(id)
    }
    fun getAllBookmarkedPosters(): Flow<List<SayingEntity>> {
        return bookmarkDao.getAll()
    }

    suspend fun insertBookmarkPoster(sayingEntity: SayingEntity) {
        return bookmarkDao.insert(sayingEntity)
    }

    suspend fun deleteBookmarkPoster(id: String) {
        return bookmarkDao.delete(id)
    }
}