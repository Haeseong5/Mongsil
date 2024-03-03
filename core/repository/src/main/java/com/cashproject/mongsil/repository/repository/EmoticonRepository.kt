package com.cashproject.mongsil.repository.repository

import com.cashproject.mongsil.repository.model.EmoticonModel

interface EmoticonRepository {
    suspend fun getEmoticon(id: Int): EmoticonModel?

    suspend fun getEmoticons(): List<EmoticonModel>
}