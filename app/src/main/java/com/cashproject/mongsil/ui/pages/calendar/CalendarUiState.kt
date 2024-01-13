package com.cashproject.mongsil.ui.pages.calendar

import com.cashproject.mongsil.ui.model.Emoticon

data class CalendarUiState(
    val calendarUiModel: List<CalendarUiModel> = emptyList(),
    val emoticons: List<Emoticon> = emptyList(),
)

// TODO DEFAULT 화면 네이밍.. 질문하기
enum class CalendarScreenType {
    DEFAULT, LIST
}

val defaultCalendarScreenType = CalendarScreenType.DEFAULT