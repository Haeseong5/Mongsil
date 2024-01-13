package com.cashproject.mongsil.data.api

import com.cashproject.mongsil.data.api.dto.EmoticonResponse
import retrofit2.http.GET

interface EmoticonApi {

    @GET("api/emoticon.json")
    suspend fun getEmoticons(): List<EmoticonResponse>
}