package com.cashproject.mongsil.model.remote

import com.cashproject.mongsil.model.data.Saying

class SayingService : SayingApi {

    override suspend fun getSayings(): List<Saying> {
        return ApiProvider.of(SayingApi::class).getSayings()
    }

}