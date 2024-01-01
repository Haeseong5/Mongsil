package com.cashproject.mongsil.ui.main.model

import com.cashproject.mongsil.extension.toLocalDate
import com.cashproject.mongsil.repository.model.DailyEmoticon
import java.time.LocalDate

data class CalendarUiModel(
    val date : LocalDate = LocalDate.now(),
    val emotionId: Int,
)

fun DailyEmoticon.toUiModel(): CalendarUiModel {
    return CalendarUiModel(
        emotionId = emoticon.id,
        date = date.toLocalDate()
    )
}