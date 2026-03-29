package com.cashproject.mongsil.kmp.screen.diarychart

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.screen.diarychart.model.DiaryChartItem
import com.cashproject.mongsil.kmp.screen.diarychart.model.DiaryChartUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class DiaryChartViewModel(
    private val diaryRepository: DiaryRepository,
    private val emoticonRepository: EmoticonRepository,
    private val getWordCloudUseCase: GetWordCloudUseCase,
    initialYear: Int,
    initialMonth: Int,
) : BaseViewModel() {

    @OptIn(ExperimentalTime::class)
    private val currentDateTime =
        kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    private val currentYear = currentDateTime.year
    private val currentMonth = currentDateTime.monthNumber

    private val _uiState = MutableStateFlow(
        DiaryChartUiState(
            year = initialYear,
            month = initialMonth,
            canMoveNextMonth = canMoveNextMonth(initialYear, initialMonth)
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadMonthStatistics(initialYear, initialMonth)
        loadStreak()
        loadWordCloud(initialYear, initialMonth)
    }

    fun moveToPreviousMonth() {
        val state = _uiState.value
        val (nextYear, nextMonth) = shiftMonth(state.year, state.month, -1)
        updateMonth(nextYear, nextMonth)
    }

    fun moveToNextMonth() {
        val state = _uiState.value
        if (!canMoveNextMonth(state.year, state.month)) {
            return
        }
        val (nextYear, nextMonth) = shiftMonth(state.year, state.month, 1)
        updateMonth(nextYear, nextMonth)
    }

    private fun updateMonth(year: Int, month: Int) {
        _uiState.update {
            it.copy(
                year = year,
                month = month,
                canMoveNextMonth = canMoveNextMonth(year, month)
            )
        }
        loadMonthStatistics(year, month)
        loadWordCloud(year, month)
    }

    private fun loadMonthStatistics(year: Int, month: Int) {
        viewModelScope.launch(exceptionHandler) {
            val emoticons = emoticonRepository.getEmoticons()

            val countMap = diaryRepository
                .getDiariesByYearMonth(year, month)
                .mapNotNull { it.emoticonId?.toInt() }
                .groupingBy { it }
                .eachCount()

            val items = countMap
                .mapNotNull { (emoticonId, count) ->
                    val emoticon = emoticons[emoticonId] ?: return@mapNotNull null
                    DiaryChartItem(
                        emoticonId = emoticonId,
                        image = emoticon.image,
                        title = emoticon.title,
                        count = count,
                        barColorHex = emoticon.backgroundColor
                    )
                }
                .sortedWith(compareByDescending<DiaryChartItem> { it.count }.thenBy { it.emoticonId })

            _uiState.update { it.copy(items = items) }
        }
    }

    private fun loadStreak() {
        viewModelScope.launch(exceptionHandler) {
            val today = currentDateTime.date
            val sortedDates = diaryRepository.getAllDiaries()
                .map { LocalDate(it.year, it.month, it.day) }
                .distinct()
                .sortedDescending()

            val latestDate = sortedDates.firstOrNull() ?: return@launch
            val yesterday = today.minus(1, DateTimeUnit.DAY)
            if (latestDate < yesterday) return@launch

            var streak = 0
            var expected = latestDate
            for (date in sortedDates) {
                if (date == expected) {
                    streak++
                    expected = expected.minus(1, DateTimeUnit.DAY)
                } else {
                    break
                }
            }
            _uiState.update { it.copy(currentStreak = streak) }
        }
    }

    private fun loadWordCloud(year: Int, month: Int) {
        viewModelScope.launch(exceptionHandler) {
            val items = getWordCloudUseCase(year, month)
            _uiState.update { it.copy(wordCloudItems = items) }
        }
    }

    private fun canMoveNextMonth(year: Int, month: Int): Boolean {
        return year < currentYear || (year == currentYear && month < currentMonth)
    }

    private fun shiftMonth(year: Int, month: Int, offset: Int): Pair<Int, Int> {
        val rawMonth = month + offset
        return when {
            rawMonth > 12 -> year + 1 to 1
            rawMonth < 1 -> year - 1 to 12
            else -> year to rawMonth
        }
    }
}
