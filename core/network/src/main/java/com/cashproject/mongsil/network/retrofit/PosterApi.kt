package com.cashproject.mongsil.network.retrofit

import com.cashproject.mongsil.network.model.PosterResponse
import retrofit2.http.GET


//https://haeseong5.github.io/api/saying.json
interface PosterApi {
    @GET("api/saying.json")
    suspend fun getAllPosters(): List<PosterResponse>
}