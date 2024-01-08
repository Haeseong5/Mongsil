package com.cashproject.mongsil.ui.pages.calendar

data class CalendarUiState(
    val calendarUiModel: List<CalendarUiModel> = emptyList(),
)

// TODO DEFAULT 화면 네이밍.. 질문하기
enum class CalendarScreenType {
    DEFAULT, LIST
}

val defaultCalendarScreenType = CalendarScreenType.DEFAULT