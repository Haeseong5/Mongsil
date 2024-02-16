package com.cashproject.mongsil.repository

import com.cashproject.mongsil.database.BookmarkDataSource
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.repository.model.toLegacy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepository(
    private val bookmarkService: BookmarkDataSource = BookmarkDataSource(),
    private val posterRepository: PosterRepository = PosterRepository(),
) {
    suspend fun loadBookmarkedPosters(): Flow<List<Poster>> {
        val posters = posterRepository.getAllPosters()
        val bookmarkPosters = bookmarkService.getAllBookmarkedPosters()
            .map { ids ->
                posters.filter {
                    ids.map { entity -> entity.docId }.contains(it.id)
                }
            }

        return bookmarkPosters
    }

    suspend fun bookmark(poster: Poster) {
        bookmarkService.insertBookmarkPoster(poster.toLegacy())
    }

    suspend fun unlike(id: String) {
        bookmarkService.deleteBookmarkPoster(id)
    }
}