package com.cashproject.mongsil.database.model

import java.util.Date

data class DiaryEmoticonEntity(
    val emoticonId: Int = 0,
    val date: Date = Date(),
)

fun List<CommentEntity>.toDailyEmoticons(): List<DiaryEmoticonEntity> {
    return map {
        DiaryEmoticonEntity(
            emoticonId = it.emotion,
            date = it.date
        )
    }
}