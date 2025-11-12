package com.cashproject.mongsil.repository.model

import com.cashproject.mongsil.database.model.CommentEntity
import java.util.Date

data class DailyEmoticon(
    val emoticonId: Int = 0,
    val date: Date = Date(),
)

fun List<CommentEntity>.toDailyEmoticons(): List<DailyEmoticon> {
    return map {
        DailyEmoticon(
            emoticonId = it.emotion,
            date = it.date
        )
    }
}