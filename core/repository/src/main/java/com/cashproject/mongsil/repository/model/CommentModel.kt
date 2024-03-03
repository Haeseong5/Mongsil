package com.cashproject.mongsil.repository.model

import com.cashproject.mongsil.database.model.CommentEntity
import java.util.Date

data class CommentModel(
    val id: Int = 0,
    val content: String = "",
    val emoticonId: Int,
    val time: Date = Date(),
    val date: Date = Date(),
)

fun List<CommentEntity>.toDomain(): List<CommentModel> {
    return map {
        CommentModel(
            id = it.id,
            content = it.content,
            emoticonId = it.emotion,
            time = it.writeTime,
            date = it.date
        )
    }
}

fun List<CommentModel>.toEntity(): List<CommentEntity> {
    return map {
        CommentEntity(
            id = it.id,
            content = it.content,
            emotion = it.emoticonId,
            writeTime = it.time,
            date = it.date
        )
    }
}

fun CommentModel.toEntity(): CommentEntity {
    return CommentEntity(
        id = id,
        content = content,
        emotion = emoticonId,
        writeTime = time,
        date = date
    )
}
