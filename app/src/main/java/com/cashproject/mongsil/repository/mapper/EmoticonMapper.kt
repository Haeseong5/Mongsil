package com.cashproject.mongsil.repository.mapper

import com.cashproject.mongsil.data.api.dto.EmoticonResponse
import com.cashproject.mongsil.extension.toColor
import com.cashproject.mongsil.ui.model.Emoticon

fun List<EmoticonResponse>.toEmoticon(): List<Emoticon> {
    return map {
        Emoticon(
            id = it.id,
            title = it.title,
            imageUrl = it.imageUrl,
            textColor = it.textColor.toColor(),
            backgroundColor = it.backgroundColor.toColor()
        )
    }
}
