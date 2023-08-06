package com.cashproject.mongsil.data.repository.mapper

import com.cashproject.mongsil.data.api.dto.PosterResponse
import com.cashproject.mongsil.data.repository.model.Poster

fun List<PosterResponse>.toPosters(): List<Poster> {
    return map {
        Poster(
            id = it.id,
            image = it.image,
            squareImage = it.squareImage,
        )
    }
}