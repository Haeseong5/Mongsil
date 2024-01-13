package com.cashproject.mongsil.data.repository

import com.cashproject.mongsil.ui.model.Emoticon

interface EmoticonRepository {
    suspend fun getEmoticon(id: Int): Emoticon?

    suspend fun getEmoticons(): List<Emoticon>
}