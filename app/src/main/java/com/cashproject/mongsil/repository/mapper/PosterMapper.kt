package com.cashproject.mongsil.repository.mapper


import com.cashproject.mongsil.network.model.PosterResponse
import com.cashproject.mongsil.repository.model.Poster

fun List<PosterResponse>.toPosters(): List<Poster> {
    return map {
        Poster(
            id = it.id,
            image = it.image,
            squareImage = it.squareImage,
        )
    }
}

//fun List<SayingEntity>.toDomain(): List<Poster> {
//    return this.map {
//        Poster(
//            id = it.docId,
//            image = it.image,
//            squareImage = it.squareImage
//        )
//    }
//}