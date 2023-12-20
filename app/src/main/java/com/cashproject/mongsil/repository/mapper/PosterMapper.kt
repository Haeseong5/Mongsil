package com.cashproject.mongsil.repository.mapper

import com.cashproject.mongsil.data.api.dto.PosterResponse
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