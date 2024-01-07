package com.cashproject.mongsil.repository.model

import android.os.Parcelable
import com.cashproject.mongsil.data.api.dto.PosterResponse
import com.cashproject.mongsil.data.db.entity.SayingEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Poster(
    val id: String,
    val image: String,
    val squareImage: String,
) : Parcelable

fun List<Poster>.findPosition(id: String): Int {
    return indexOfFirst { it.id == id }.coerceIn(0, size)
}

fun List<PosterResponse>.toDomain(): List<Poster> {
    return this.map {
        Poster(
            id = it.id,
            image = it.image,
            squareImage = it.squareImage
        )
    }
}

@Deprecated("toDomain() 함수로 변경해야 함.")
fun List<Poster>.toLegacy(): List<SayingEntity> {
    return this.map {
        SayingEntity(
            docId = it.id,
            image = it.image,
            squareImage = it.squareImage
        )
    }
}

@Deprecated("toDomain() 함수로 변경해야 함.")
fun Poster.toLegacy(): SayingEntity {
    return SayingEntity(
        docId = this.id,
        image = this.image,
        squareImage = this.squareImage
    )
}