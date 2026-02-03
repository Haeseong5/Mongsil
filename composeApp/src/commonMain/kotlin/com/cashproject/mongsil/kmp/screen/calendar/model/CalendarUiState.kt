package com.cashproject.mongsil.kmp.screen.calendar.model

import com.cashproject.mongsil.kmp.model.Emoticon
import kotlinx.datetime.LocalDate

data class CalendarUiState(
    val currentYear: Int = 2025,
    val currentMonth: Int = 1,
    val calendarRecords: List<CalendarRecord> = emptyList(),
    val emoticons: List<Emoticon> = emptyList()
)

data class CalendarRecord(
    val date: LocalDate,
    val emotionId: Int
)

