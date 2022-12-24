package com.cashproject.mongsil.model.remote

import com.cashproject.mongsil.data.db.entity.Saying

class SayingService : SayingApi {

    override suspend fun getSayings(): List<Saying> {
        return ApiProvider.of(SayingApi::class).getSayings()
    }

}