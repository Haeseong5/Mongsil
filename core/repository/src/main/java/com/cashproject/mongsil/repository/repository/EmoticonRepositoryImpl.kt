package com.cashproject.mongsil.repository.repository

import com.cashproject.mongsil.network.EmoticonDataSource
import com.cashproject.mongsil.repository.model.EmoticonModel
import com.cashproject.mongsil.repository.model.toEmoticonModel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class EmoticonRepositoryImpl(
    private val emoticonService: EmoticonDataSource = EmoticonDataSource
) : EmoticonRepository {

    companion object {
        private var cachedEmoticons: List<EmoticonModel>? = null
    }

    private val mutex = Mutex()

    override suspend fun getEmoticon(id: Int): EmoticonModel? {
        return mutex.withLock {
            val e = cachedEmoticons
            if (e != null) {
                e.find { it.id == id }
            } else {
                val emotions = getEmoticonsFromDataSource()
                emotions.find { it.id == id }
            }
        }
    }

    override suspend fun getEmoticons(): List<EmoticonModel> {
        return mutex.withLock {
            val e = cachedEmoticons
            if (e == null) {
                val result = getEmoticonsFromDataSource()
                cachedEmoticons = result
                result
            } else {
                e
            }
        }
    }

    private suspend fun getEmoticonsFromDataSource(): List<EmoticonModel> {
        return emoticonService.getEmoticons().toEmoticonModel()
    }
}