package com.cashproject.mongsil.ui.pages.diary.model

import android.os.Parcelable
import com.cashproject.mongsil.database.model.SayingEntity
import com.cashproject.mongsil.repository.model.PosterModel
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


fun PosterModel.toPoster(): Poster {
    return Poster(
        id = this.id,
        image = this.image,
        squareImage = this.squareImage
    )
}

fun List<PosterModel>.toPoster(): List<Poster> {
    return this.map {
        Poster(
            id = it.id,
            image = it.image,
            squareImage = it.squareImage
        )
    }
}

fun List<Poster>.toDomain(): List<PosterModel> {
    return this.map {
        PosterModel(
            id = it.id,
            image = it.image,
            squareImage = it.squareImage
        )
    }
}

fun Poster.toDomain(): PosterModel {
    return PosterModel(
        id = this.id,
        image = this.image,
        squareImage = this.squareImage
    )
}