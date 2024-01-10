package com.cashproject.mongsil.data.api.dto

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PosterResponse(
    val id: String,
    val image: String,
    @ColumnInfo(name = "s")
    val squareImage: String,
)