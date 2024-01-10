package com.cashproject.mongsil.data.api

import com.cashproject.mongsil.data.api.dto.PosterResponse
import retrofit2.http.GET


//https://haeseong5.github.io/api/saying.json
interface PosterApi {
    @GET("api/saying.json")
    suspend fun getAllPosters(): List<PosterResponse>
}