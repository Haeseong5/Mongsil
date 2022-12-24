package com.cashproject.mongsil.model.remote

import com.cashproject.mongsil.data.db.entity.SayingEntity
import retrofit2.http.GET


//https://haeseong5.github.io/api/saying.json
interface SayingApi {
    @GET("api/saying.json")
    suspend fun getSayings(): List<SayingEntity>
}