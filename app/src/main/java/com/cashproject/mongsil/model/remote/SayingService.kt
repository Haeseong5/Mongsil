package com.cashproject.mongsil.model.remote

import com.cashproject.mongsil.data.db.entity.SayingEntity

class SayingService : SayingApi {

    override suspend fun getSayings(): List<SayingEntity> {
        return ApiProvider.of(SayingApi::class).getSayings()
    }

}