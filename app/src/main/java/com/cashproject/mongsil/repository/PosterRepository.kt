package com.cashproject.mongsil.repository

import com.cashproject.mongsil.data.service.PosterService
import com.cashproject.mongsil.repository.mapper.toPosters
import com.cashproject.mongsil.repository.model.Poster
import java.util.concurrent.atomic.AtomicReference

class PosterRepository(
    private val posterService: PosterService = PosterService
) {

    companion object {
        private val posters: AtomicReference<List<Poster>> = AtomicReference(emptyList())
    }

    suspend fun getAllPosters(): List<Poster> {
        if (posters.get().isEmpty()) {
            val allPosters = posterService.getAllPosters().toPosters()
            posters.set(allPosters)
        }
        return posters.get()
    }

    suspend fun getRandomSinglePoster(): Poster {
        val poster = getAllPosters().getOrNull(0)
        requireNotNull(poster)
        return poster
    }

}
