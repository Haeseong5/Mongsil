package com.cashproject.mongsil.kmp.screen.calendar.model

import kotlinx.datetime.LocalDate

data class CalendarUiState(
    val currentYear: Int = 2025,
    val currentMonth: Int = 1,
    val calendarRecords: List<CalendarRecord> = emptyList(),
    val emoticons: List<EmoticonData> = emptyList()
)

data class CalendarRecord(
    val date: LocalDate,
    val emotionId: Int
)

data class EmoticonData(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val textColor: String,
    val backgroundColor: String
)

