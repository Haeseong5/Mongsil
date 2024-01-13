package com.cashproject.mongsil.repository

import com.cashproject.mongsil.data.repository.EmoticonRepository
import com.cashproject.mongsil.data.service.EmoticonService
import com.cashproject.mongsil.repository.mapper.toEmoticon
import com.cashproject.mongsil.ui.model.Emoticon

class EmoticonRepositoryImpl(
    private val emoticonService: EmoticonService = EmoticonService
) : EmoticonRepository {

    private var cachedEmoticons: List<Emoticon>? = null
    override suspend fun getEmoticon(id: Int): Emoticon? {
        return if (cachedEmoticons != null) {
            cachedEmoticons!!.find { it.id == id }
        } else {
            val emotions = emoticonService.getEmoticons().toEmoticon()
            emotions.find { it.id == id }
        }
    }

    override suspend fun getEmoticons(): List<Emoticon> {
        val result = cachedEmoticons ?: emoticonService.getEmoticons().toEmoticon()
        cachedEmoticons = result
        return result
    }
}