package com.cashproject.mongsil.repository

import com.cashproject.mongsil.data.repository.EmoticonRepository
import com.cashproject.mongsil.data.service.EmoticonService
import com.cashproject.mongsil.repository.mapper.toEmoticon
import com.cashproject.mongsil.ui.model.Emoticon
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class EmoticonRepositoryImpl(
    private val emoticonService: EmoticonService = EmoticonService
) : EmoticonRepository {

    companion object {
        private var cachedEmoticons: List<Emoticon>? = null
    }

    private val mutex = Mutex()

    override suspend fun getEmoticon(id: Int): Emoticon? {
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

    override suspend fun getEmoticons(): List<Emoticon> {
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

    private suspend fun getEmoticonsFromDataSource(): List<Emoticon> {
        return emoticonService.getEmoticons().toEmoticon()
    }
}