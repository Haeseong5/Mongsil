package com.cashproject.mongsil.network

import com.cashproject.mongsil.network.model.PosterResponse
import com.cashproject.mongsil.network.retrofit.PosterApi
import kotlinx.serialization.ExperimentalSerializationApi

object PosterDataSource : PosterApi {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getAllPosters(): List<PosterResponse> {
        return ApiProvider.of(PosterApi::class).getAllPosters()
    }
}