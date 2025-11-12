package com.cashproject.mongsil.repository.model

import com.cashproject.mongsil.network.model.EmoticonResponse


data class EmoticonModel(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val textColor: String,
    val backgroundColor: String
)

fun List<EmoticonResponse>.toEmoticonModel(): List<EmoticonModel> {
    return this.map {
        EmoticonModel(
            id = it.id,
            imageUrl = it.imageUrl,
            title = it.title,
            textColor = it.textColor,
            backgroundColor = it.backgroundColor
        )
    }
}
