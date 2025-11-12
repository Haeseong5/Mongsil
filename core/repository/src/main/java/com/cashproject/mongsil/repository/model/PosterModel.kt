package com.cashproject.mongsil.repository.model

import com.cashproject.mongsil.database.model.SayingEntity
import com.cashproject.mongsil.network.model.PosterResponse

data class PosterModel(
    val id: String,
    val image: String,
    val squareImage: String,
)

fun List<PosterResponse>.toPosterModel(): List<PosterModel> {
    return this.map {
        PosterModel(
            id = it.id,
            image = it.image,
            squareImage = it.squareImage
        )
    }
}

fun PosterModel.toSayingEntity(): SayingEntity {
    return SayingEntity(
        docId = this.id,
        image = this.image,
        squareImage = this.squareImage
    )
}