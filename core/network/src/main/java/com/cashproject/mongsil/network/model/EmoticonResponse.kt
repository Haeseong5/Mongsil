package com.cashproject.mongsil.network.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class EmoticonResponse(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val textColor: String,
    val backgroundColor: String
)
