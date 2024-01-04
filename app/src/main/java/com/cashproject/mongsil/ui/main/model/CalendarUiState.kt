package com.cashproject.mongsil.ui.main.model

data class CalendarUiState(
    val calendarUiModel: List<CalendarUiModel> = emptyList(),
    val screenType: CalendarScreenType = CalendarScreenType.DEFAULT
)

enum class CalendarScreenType {
    DEFAULT, LIST
}