package com.cashproject.mongsil.data.repository

import com.cashproject.mongsil.data.service.BookmarkService
import com.cashproject.mongsil.data.repository.model.Poster

class BookmarkRepository(
    private val bookmarkService: BookmarkService,
    private val memoryCacheRepository: MemoryCacheRepository,
) {
    suspend fun getBookmarkedPosters(): List<Poster> {
        val bookmarkIds = bookmarkService.getAllBookmarkedPosters()
            .map { it.docId }
        val posters = memoryCacheRepository.getAllPosters()

        return posters.filter {
            bookmarkIds.contains(it.id)
        }
    }

    suspend fun addBookmark(id: String) {
        bookmarkService.insertBookmarkPoster(id)
    }

    suspend fun deleteBookmark(id: String) {
        bookmarkService.deleteBookmarkPoster(id)
    }
}