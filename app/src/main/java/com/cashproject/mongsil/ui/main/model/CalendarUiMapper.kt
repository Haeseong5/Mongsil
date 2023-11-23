package com.cashproject.mongsil.ui.main.model

import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.util.timeMillisToLocalDate

object CalendarUiMapper {
    fun mapper(from: List<CommentEntity>): List<CalendarUiModel> {
        return from.map {
            CalendarUiModel(
                date = it.date.time.timeMillisToLocalDate(),
                emotionId  = it.emotion
            )
        }
    }
}