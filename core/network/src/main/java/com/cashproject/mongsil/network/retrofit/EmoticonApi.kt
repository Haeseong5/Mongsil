package com.cashproject.mongsil.network.retrofit

import com.cashproject.mongsil.network.model.EmoticonResponse
import retrofit2.http.GET

interface EmoticonApi {

    @GET("api/emoticon.json")
    suspend fun getEmoticons(): List<EmoticonResponse>
}