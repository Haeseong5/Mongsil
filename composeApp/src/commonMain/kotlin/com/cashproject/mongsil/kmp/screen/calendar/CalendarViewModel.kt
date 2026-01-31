package com.cashproject.mongsil.kmp.screen.calendar

import androidx.lifecycle.ViewModel
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // 현재 년월로 초기화
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        _uiState.update { it.copy(currentYear = now.year, currentMonth = now.monthNumber) }
    }

    fun updateYearMonth(year: Int, month: Int) {
        _uiState.update { it.copy(currentYear = year, currentMonth = month) }
    }
}