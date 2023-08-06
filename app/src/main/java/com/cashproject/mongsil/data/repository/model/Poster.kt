package com.cashproject.mongsil.data.repository.model

import com.cashproject.mongsil.data.api.dto.PosterResponse
import com.cashproject.mongsil.data.db.entity.SayingEntity

data class Poster(
    val id: String,
    val image: String,
    val squareImage: String,
)

fun List<PosterResponse>.toDomain(): List<Poster> {
    return this.map {
        Poster(
            id = it.id,
            image = it.image,
            squareImage = it.squareImage
        )
    }
}


fun List<Poster>.toLegacy(): List<SayingEntity> {
    return this.map {
        SayingEntity(
            docId = it.id,
            image = it.image,
            squareImage = it.squareImage
        )
    }
}


fun Poster.toLegacy(): SayingEntity {
    return SayingEntity(
        docId = this.id,
        image = this.image,
        squareImage = this.squareImage
    )
}