package com.cashproject.mongsil.network.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PosterResponse(
    val id: String,
    val image: String,
    val squareImage: String,
)