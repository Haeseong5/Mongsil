package com.cashproject.mongsil.ui.pages.calendar

import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.extension.timeMillisToLocalDate

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