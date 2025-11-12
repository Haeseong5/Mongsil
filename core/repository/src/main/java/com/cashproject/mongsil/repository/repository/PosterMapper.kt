package com.cashproject.mongsil.repository.repository


import com.cashproject.mongsil.database.model.SayingEntity
import com.cashproject.mongsil.network.model.PosterResponse

fun List<PosterResponse>.toSayingEntities(): List<SayingEntity> {
    return this.map {
        SayingEntity(
            docId = it.id,
            image = it.image,
            squareImage = it.squareImage
        )
    }
}

fun PosterResponse.toSayingEntity(): SayingEntity {
    return SayingEntity(
        docId = this.id,
        image = this.image,
        squareImage = this.squareImage
    )
}