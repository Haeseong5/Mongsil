package com.cashproject.mongsil.ui.model

import androidx.compose.ui.graphics.Color
import com.cashproject.mongsil.common.extensions.toColor
import com.cashproject.mongsil.repository.model.EmoticonModel


data class Emoticon(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val textColor: Color,
    val backgroundColor: Color,
)


fun List<EmoticonModel>.toEmoticon(): List<Emoticon> {
    return this.map {
        Emoticon(
            id = it.id,
            title = it.title,
            imageUrl = it.imageUrl,
            textColor = it.textColor.toColor(),
            backgroundColor = it.backgroundColor.toColor()
        )
    }
}

//enum class Type {
//    FREE, CASH, AD,
//}