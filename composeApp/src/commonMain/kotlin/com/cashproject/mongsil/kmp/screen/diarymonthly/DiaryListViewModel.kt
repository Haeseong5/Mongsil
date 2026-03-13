package com.cashproject.mongsil.kmp.screen.diarymonthly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryMonthlyItem
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryMonthlyUiState
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryMonthlyUiState.Companion.PAGE_SIZE
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiarySortOrder
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryViewMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class DiaryListViewModel(
    private val diaryRepository: DiaryRepository,
    private val emoticonRepository: EmoticonRepository,
    initialYear: Int,
    initialMonth: Int,
) : ViewModel() {

    @OptIn(ExperimentalTime::class)
    private val currentDateTime =
        kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    private val currentYear = currentDateTime.year
    private val currentMonth = currentDateTime.monthNumber

    private val _uiState = MutableStateFlow(
        DiaryMonthlyUiState(
            year = initialYear,
            month = initialMonth,
            canMoveNextMonth = canMoveNextMonth(initialYear, initialMonth),
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadMonthDiaries(initialYear, initialMonth)
    }

    fun switchViewMode() {
        val newMode = when (_uiState.value.viewMode) {
            DiaryViewMode.MONTHLY -> DiaryViewMode.ALL
            DiaryViewMode.ALL -> DiaryViewMode.MONTHLY
        }
        _uiState.update { it.copy(viewMode = newMode, displayCount = PAGE_SIZE) }
        if (newMode == DiaryViewMode.ALL && _uiState.value.allDiaries.isEmpty()) {
            loadAllDiaries()
        }
    }

    fun loadMoreDiaries() {
        _uiState.update { state ->
            if (!state.hasMorePages) return@update state
            state.copy(displayCount = state.displayCount + PAGE_SIZE)
        }
    }

    fun toggleSortOrder() {
        _uiState.update { state ->
            state.copy(
                sortOrder = if (state.sortOrder == DiarySortOrder.LATEST) {
                    DiarySortOrder.OLDEST
                } else {
                    DiarySortOrder.LATEST
                },
                displayCount = PAGE_SIZE,
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
        if (!canMoveNextMonth(state.year, state.month)) return
        val (nextYear, nextMonth) = shiftMonth(state.year, state.month, 1)
        updateMonth(nextYear, nextMonth)
    }

    private fun updateMonth(year: Int, month: Int) {
        _uiState.update {
            it.copy(
                year = year,
                month = month,
                canMoveNextMonth = canMoveNextMonth(year, month),
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
                            .orEmpty(),
                    )
                }

            _uiState.update { state ->
                state.copy(monthlyDiaries = sortItems(items, state.sortOrder))
            }
        }
    }

    private fun loadAllDiaries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingAll = true) }

            val emoticonMap = emoticonRepository
                .getEmoticons()
                .getOrElse { emptyList() }
                .associateBy { it.id }

            val items = diaryRepository
                .getAllDiaries()
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
                            .orEmpty(),
                    )
                }

            _uiState.update { state ->
                state.copy(
                    allDiaries = sortItems(items, state.sortOrder),
                    isLoadingAll = false,
                )
            }
        }
    }

    private fun applySorting() {
        _uiState.update { state ->
            state.copy(
                monthlyDiaries = sortItems(state.monthlyDiaries, state.sortOrder),
                allDiaries = sortItems(state.allDiaries, state.sortOrder),
            )
        }
    }

    private fun sortItems(
        items: List<DiaryMonthlyItem>,
        order: DiarySortOrder,
    ): List<DiaryMonthlyItem> = when (order) {
        DiarySortOrder.LATEST -> items.sortedWith(
            compareByDescending<DiaryMonthlyItem> { it.year }
                .thenByDescending { it.month }
                .thenByDescending { it.day },
        )

        DiarySortOrder.OLDEST -> items.sortedWith(
            compareBy<DiaryMonthlyItem> { it.year }
                .thenBy { it.month }
                .thenBy { it.day },
        )
    }

    private fun canMoveNextMonth(year: Int, month: Int): Boolean =
        year < currentYear || (year == currentYear && month < currentMonth)

    private fun shiftMonth(year: Int, month: Int, offset: Int): Pair<Int, Int> {
        val rawMonth = month + offset
        return when {
            rawMonth > 12 -> year + 1 to 1
            rawMonth < 1 -> year - 1 to 12
            else -> year to rawMonth
        }
    }
}
