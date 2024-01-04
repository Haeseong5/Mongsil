package com.cashproject.mongsil.repository

import android.util.Log
import com.cashproject.mongsil.data.service.PosterService
import com.cashproject.mongsil.repository.mapper.toPosters
import com.cashproject.mongsil.repository.model.Poster
import java.util.Date
import java.util.concurrent.atomic.AtomicReference
import kotlin.random.Random

class PosterRepository(
    private val posterService: PosterService = PosterService
) {
    companion object {
        private val posters: AtomicReference<List<Poster>> = AtomicReference(emptyList())
        private val hashMap = HashMap<Long, Int>()
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

    fun getRandomSaying(
        date: Date,
        posters: List<Poster>
    ): Poster {
        return try {
            val day = date.time
            val sayings = posters
            val cachedIdx = hashMap[day]
            if (cachedIdx == null) {
                val randomIdx = Random.nextInt(sayings.size)
                hashMap[day] = randomIdx
                sayings[randomIdx]
            } else {
                sayings[cachedIdx]
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.localizedMessage)
//            errorSubject.onNext(e)
            Poster(id = "", image = "", squareImage = "")
        }
    }

}
