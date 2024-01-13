package com.cashproject.mongsil.ui.pages.diary

import com.cashproject.mongsil.data.db.entity.CommentEntity
import java.util.Date

data class Comment(
    val id: Int = 0,
    val content: String = "",
    val emoticonId: Int,//Emoticon = Emoticon(),//defaultEmoticon,
    val time: Date = Date(), //작성 시간. 날짜 고려 X
    val date: Date = Date(), //어떤 날짜에 작성했는 지. 시간 고려 X
)

fun List<CommentEntity>.toDomain(): List<Comment> {
    return map {
        Comment(
            id = it.id,
            content = it.content,
            emoticonId = it.emotion,
            time = it.writeTime,
            date = it.date
        )
    }
}

fun Comment.toEntity(): CommentEntity {
    return CommentEntity(
        id = id,
        content = content,
        emotion = emoticonId,
        writeTime = time,
        date = date
    )
}