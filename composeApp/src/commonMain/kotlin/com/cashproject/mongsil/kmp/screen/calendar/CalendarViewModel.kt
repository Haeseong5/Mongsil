package com.cashproject.mongsil.kmp.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.repository.DiaryRepository
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarRecord
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalendarViewModel(
    private val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // 현재 년월로 초기화
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        _uiState.update { it.copy(currentYear = now.year, currentMonth = now.monthNumber) }
        loadDiariesForCurrentMonth()
    }

    fun updateYearMonth(year: Int, month: Int) {
        _uiState.update { it.copy(currentYear = year, currentMonth = month) }
        loadDiariesForMonth(year, month)
    }

    private fun loadDiariesForCurrentMonth() {
        val currentState = _uiState.value
        loadDiariesForMonth(currentState.currentYear, currentState.currentMonth)
    }

    private fun loadDiariesForMonth(year: Int, month: Int) {
        viewModelScope.launch {
            val diaries = diaryRepository.getDiariesByYearMonth(year, month)
            
            val calendarRecords = diaries.map { diary ->
                CalendarRecord(
                    date = LocalDate(
                        year = diary.year.toInt(),
                        monthNumber = diary.month.toInt(),
                        dayOfMonth = diary.day.toInt()
                    ),
                    emotionId = 0 // 현재는 감정 ID를 사용하지 않음
                )
            }
            
            _uiState.update { it.copy(calendarRecords = calendarRecords) }
        }
    }
}