package com.cashproject.mongsil.kmp.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarRecord
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiEvent
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalendarViewModel(
    private val diaryRepository: DiaryRepository,
    private val emoticonRepository: EmoticonRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // 현재 년월로 초기화
        val now = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        _uiState.update { 
            it.copy(
                today = now.date,
                currentYear = now.year, 
                currentMonth = now.monthNumber
            ) 
        }
        loadDiariesForCurrentMonth()
        loadEmoticons()
    }

    private fun loadEmoticons() {
        viewModelScope.launch {
            emoticonRepository.getEmoticons()
                .onSuccess { emoticons ->
                    println("++## 이모티콘 로드 성공: ${emoticons}개")
                    _uiState.update {
                        it.copy(
                            emoticons = emoticons
                        )
                    }
                    // TODO: UiState에 이모티콘 저장
                }
                .onFailure { error ->
                    println("++## 이모티콘 로드 실패: ${error.message}")
                }
        }
    }

    fun onEvent(event: CalendarUiEvent) {
        when (event) {
            is CalendarUiEvent.ShowAndHideYearMonthPicker -> {
                _uiState.update { it.copy(isShownYearMonthPicker = event.isShow) }
            }

            is CalendarUiEvent.OnYearMonthPickerSelected -> {
                _uiState.update {
                    it.copy(
                        isShownYearMonthPicker = false,
                        currentYear = event.year,
                        currentMonth = event.month
                    )
                }
            }
        }

    }

    fun updateYearMonth(year: Int, month: Int) {
        _uiState.update { it.copy(currentYear = year, currentMonth = month) }
        // 현재 월 + 인접 월(이전/다음)을 미리 로드하여 스와이프 시 즉시 표시
        loadDiariesForMonth(year, month)
        loadDiariesForMonth(prevYear(year, month), prevMonth(year, month))
        loadDiariesForMonth(nextYear(year, month), nextMonth(year, month))
    }

    private fun loadDiariesForCurrentMonth() {
        val currentState = _uiState.value
        updateYearMonth(currentState.currentYear, currentState.currentMonth)
    }

    private fun loadDiariesForMonth(year: Int, month: Int) {
        viewModelScope.launch {
            val diaries = diaryRepository.getDiariesByYearMonth(year, month)

            val newRecords = diaries.map { diary ->
                CalendarRecord(
                    date = LocalDate(
                        year = diary.year.toInt(),
                        monthNumber = diary.month.toInt(),
                        dayOfMonth = diary.day.toInt()
                    ),
                    emotionId = diary.emoticonId?.toInt() ?: 0
                )
            }

            _uiState.update { state ->
                // 해당 월 기존 데이터를 제거 후 새 데이터로 교체 (누적)
                val merged = state.calendarRecords
                    .filterNot { it.date.year == year && it.date.monthNumber == month }
                    .plus(newRecords)
                state.copy(calendarRecords = merged)
            }
        }
    }

    private fun prevYear(year: Int, month: Int) = if (month == 1) year - 1 else year
    private fun prevMonth(year: Int, month: Int) = if (month == 1) 12 else month - 1
    private fun nextYear(year: Int, month: Int) = if (month == 12) year + 1 else year
    private fun nextMonth(year: Int, month: Int) = if (month == 12) 1 else month + 1
}