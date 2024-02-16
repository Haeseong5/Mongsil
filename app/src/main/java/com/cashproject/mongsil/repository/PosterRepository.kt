package com.cashproject.mongsil.repository

import com.cashproject.mongsil.common.utils.printErrorLog
import com.cashproject.mongsil.network.PosterDataSource
import com.cashproject.mongsil.network.retrofit.PosterApi
import com.cashproject.mongsil.repository.mapper.toPosters
import com.cashproject.mongsil.repository.model.Poster
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import kotlin.random.Random

class PosterRepository(
    private val posterService: PosterApi = PosterDataSource
) {
    companion object {
        private val posters: MutableStateFlow<List<Poster>> = MutableStateFlow(emptyList())
        private val hashMap = HashMap<Long, Int>()
    }

    suspend fun getAllPostersFlow(): StateFlow<List<Poster>> {
        if (posters.value.isEmpty()) {
            val allPosters = posterService.getAllPosters().toPosters()
            posters.emit(allPosters)
        }
        return posters
    }

    suspend fun getAllPosters(): List<Poster> {
        return getAllPostersFlow().value
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
            e.printErrorLog()
            Poster(id = "", image = "", squareImage = "")
        }
    }

}
