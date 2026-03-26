package com.cashproject.mongsil.kmp.screen.calendar

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.core.data.SettingRepository
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarRecord
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiEvent
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
import com.cashproject.mongsil.kmp.screen.calendar.model.ScrollTarget
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val settingRepository: SettingRepository,
) : BaseViewModel() {

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
        observeEmoticonTranslucentSetting()
    }

    private fun observeEmoticonTranslucentSetting() {
        settingRepository.isEmoticonTranslucentEnabled()
            .onEach { enabled ->
                _uiState.update { it.copy(isEmoticonTranslucent = enabled) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadEmoticons() {
        viewModelScope.launch(exceptionHandler) {
            val emoticons = emoticonRepository.getDefaultEmoticons()
            _uiState.update {
                it.copy(
                    emoticons = emoticons
                )
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
                        scrollTarget = ScrollTarget(event.year, event.month)
                    )
                }
            }

            is CalendarUiEvent.ClearScrollTarget -> {
                _uiState.update { it.copy(scrollTarget = null) }
            }
        }

    }

    fun updateYearMonth(year: Int, month: Int) {
        _uiState.update { it.copy(currentYear = year, currentMonth = month) }
        // 현재/이전/다음 월을 병렬 로드 후 state를 한 번에 업데이트 (리컴포지션 1회)
        viewModelScope.launch(exceptionHandler) {
            val monthsToLoad = listOf(
                YearMonth(year, month),
                YearMonth(prevYear(year, month), prevMonth(year, month)),
                YearMonth(nextYear(year, month), nextMonth(year, month)),
            )
            val results = monthsToLoad
                .map { ym -> async { MonthRecords(ym, loadRecords(ym.year, ym.month)) } }
                .awaitAll()

            _uiState.update { state ->
                var merged = state.calendarRecords
                results.forEach { (ym, newRecords) ->
                    merged = merged
                        .filterNot { it.date.year == ym.year && it.date.monthNumber == ym.month }
                        .plus(newRecords)
                }
                state.copy(calendarRecords = merged)
            }
        }
    }

    private data class YearMonth(val year: Int, val month: Int)
    private data class MonthRecords(val yearMonth: YearMonth, val records: List<CalendarRecord>)

    private fun loadDiariesForCurrentMonth() {
        val currentState = _uiState.value
        updateYearMonth(currentState.currentYear, currentState.currentMonth)
    }

    private suspend fun loadRecords(year: Int, month: Int): List<CalendarRecord> {
        return diaryRepository.getDiariesByYearMonth(year, month).map { diary ->
            CalendarRecord(
                date = LocalDate(
                    year = diary.year.toInt(),
                    monthNumber = diary.month.toInt(),
                    dayOfMonth = diary.day.toInt()
                ),
                emotionId = diary.emoticonId?.toInt() ?: 0
            )
        }
    }

    private fun prevYear(year: Int, month: Int) = if (month == 1) year - 1 else year
    private fun prevMonth(year: Int, month: Int) = if (month == 1) 12 else month - 1
    private fun nextYear(year: Int, month: Int) = if (month == 12) year + 1 else year
    private fun nextMonth(year: Int, month: Int) = if (month == 12) 1 else month + 1
}