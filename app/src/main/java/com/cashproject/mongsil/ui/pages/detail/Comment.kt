package com.cashproject.mongsil.ui.pages.detail

import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.data.db.entity.toEmoticon
import com.cashproject.mongsil.ui.model.Emoticon
import com.cashproject.mongsil.ui.model.Emoticons
import com.cashproject.mongsil.ui.model.defaultEmoticon
import java.util.Date

data class Comment(
    val id: Int = 0,
    val content: String = "",
    val emoticon: Emoticon = defaultEmoticon,
    val time: Date = Date(), //작성 시간. 날짜 고려 X
    val date: Date = Date(), //어떤 날짜에 작성했는 지. 시간 고려 X
)

fun List<CommentEntity>.toDomain(): List<Comment> {
    return map {
        Comment(
            id = it.id,
            content = it.content,
            emoticon = it.emotion.toEmoticon(),
            time = it.time,
            date = it.date
        )
    }
}

fun Comment.toEntity(): CommentEntity {
    return CommentEntity(
        id = id,
        content = content,
        emotion = emoticon.id,
        time = time,
        date = date
    )
}