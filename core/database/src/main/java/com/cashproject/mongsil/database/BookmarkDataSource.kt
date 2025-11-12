package com.cashproject.mongsil.database

import com.cashproject.mongsil.database.dao.BookmarkDao
import com.cashproject.mongsil.database.model.SayingEntity
import kotlinx.coroutines.flow.Flow

class BookmarkDataSource(
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