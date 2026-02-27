package com.cashproject.mongsil.kmp.screen.diarymonthly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryMonthlyItem
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryMonthlyUiState
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiarySortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DiaryMonthlyViewModel(
    private val diaryRepository: DiaryRepository,
    private val emoticonRepository: EmoticonRepository,
    initialYear: Int,
    initialMonth: Int,
) : ViewModel() {

    private val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    private val currentYear = currentDateTime.year
    private val currentMonth = currentDateTime.monthNumber

    private val _uiState = MutableStateFlow(
        DiaryMonthlyUiState(
            year = initialYear,
            month = initialMonth,
            canMoveNextMonth = canMoveNextMonth(initialYear, initialMonth)
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadMonthDiaries(initialYear, initialMonth)
    }

    fun toggleSortOrder() {
        _uiState.update {
            it.copy(
                sortOrder = if (it.sortOrder == DiarySortOrder.LATEST) {
                    DiarySortOrder.OLDEST
                } else {
                    DiarySortOrder.LATEST
                }
            )
        }
        applySorting()
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
        loadMonthDiaries(year, month)
    }

    private fun loadMonthDiaries(year: Int, month: Int) {
        viewModelScope.launch {
            val emoticonMap = emoticonRepository
                .getEmoticons()
                .getOrElse { emptyList() }
                .associateBy { it.id }

            val items = diaryRepository
                .getDiariesByYearMonth(year, month)
                .map { diary ->
                    DiaryMonthlyItem(
                        id = diary.id,
                        year = diary.year.toInt(),
                        month = diary.month.toInt(),
                        day = diary.day.toInt(),
                        content = diary.content,
                        emoticonImageUrl = diary.emoticonId
                            ?.toInt()
                            ?.let { emoticonMap[it]?.imageUrl }
                            .orEmpty()
                    )
                }

            _uiState.update { it.copy(diaries = items) }
            applySorting()
        }
    }

    private fun applySorting() {
        _uiState.update { state ->
            val sorted = when (state.sortOrder) {
                DiarySortOrder.LATEST -> state.diaries.sortedByDescending { it.day }
                DiarySortOrder.OLDEST -> state.diaries.sortedBy { it.day }
            }
            state.copy(diaries = sorted)
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
