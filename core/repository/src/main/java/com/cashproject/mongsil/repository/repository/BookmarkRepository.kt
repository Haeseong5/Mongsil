package com.cashproject.mongsil.repository.repository

import com.cashproject.mongsil.database.BookmarkDataSource
import com.cashproject.mongsil.repository.model.PosterModel
import com.cashproject.mongsil.repository.model.toSayingEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepository(
    private val bookmarkService: BookmarkDataSource = BookmarkDataSource(),
    private val posterRepository: PosterRepository = PosterRepository(),
) {
    suspend fun loadBookmarkedPosters(): Flow<List<PosterModel>> {
        val posters = posterRepository.getAllPosters()
        val bookmarkPosters = bookmarkService.getAllBookmarkedPosters()
            .map { ids ->
                posters.filter {
                    ids.map { entity -> entity.docId }.contains(it.id)
                }
            }

        return bookmarkPosters
    }

    suspend fun bookmark(poster: PosterModel) {
        bookmarkService.insertBookmarkPoster(poster.toSayingEntity())
    }

    suspend fun unlike(id: String) {
        bookmarkService.deleteBookmarkPoster(id)
    }
}