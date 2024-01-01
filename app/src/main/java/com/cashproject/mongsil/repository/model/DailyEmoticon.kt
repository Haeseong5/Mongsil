package com.cashproject.mongsil.repository.model

import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.ui.model.Emoticon
import com.cashproject.mongsil.ui.model.Emoticons
import com.cashproject.mongsil.ui.model.defaultEmoticon
import java.util.Date

data class DailyEmoticon(
    val emoticon: Emoticon = defaultEmoticon,
    val date: Date = Date(),
)

fun List<CommentEntity>.toDailyEmoticons(): List<DailyEmoticon> {
    return map {
        DailyEmoticon(
            emoticon = Emoticons.emoticons[it.emotion],
            date = it.date
        )
    }
}