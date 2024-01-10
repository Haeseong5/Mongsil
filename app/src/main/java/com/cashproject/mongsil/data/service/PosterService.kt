package com.cashproject.mongsil.data.service

import com.cashproject.mongsil.data.api.ApiProvider
import com.cashproject.mongsil.data.api.PosterApi
import com.cashproject.mongsil.data.api.dto.PosterResponse
import kotlinx.serialization.ExperimentalSerializationApi

object PosterService : PosterApi {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getAllPosters(): List<PosterResponse> {
        return ApiProvider.of(PosterApi::class).getAllPosters()
    }
}