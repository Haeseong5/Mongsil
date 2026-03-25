package com.cashproject.mongsil.kmp.screen.diarymonthly

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.database.entity.DiaryEntity
import com.cashproject.mongsil.kmp.model.Emoticon
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
) : BaseViewModel() {

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
        val needsLoad = newMode == DiaryViewMode.ALL && _uiState.value.allDiaries.isEmpty()
        _uiState.update { it.copy(viewMode = newMode, isLoadingAll = needsLoad) }
        if (needsLoad) {
            loadAllDiaries()
        }
    }

    fun loadMoreDiaries() {
        if (!_uiState.value.hasMorePages) return
        loadAllDiariesPage(offset = _uiState.value.allDiaries.size)
    }

    fun toggleSortOrder() {
        val newOrder = if (_uiState.value.sortOrder == DiarySortOrder.LATEST) {
            DiarySortOrder.OLDEST
        } else {
            DiarySortOrder.LATEST
        }
        _uiState.update { state ->
            state.copy(
                sortOrder = newOrder,
                monthlyDiaries = sortItems(state.monthlyDiaries, newOrder),
                allDiaries = emptyList(),
                allDiariesTotalCount = 0,
            )
        }
        if (_uiState.value.viewMode == DiaryViewMode.ALL) {
            loadAllDiaries()
        }
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
        viewModelScope.launch(exceptionHandler) {
            val emoticonMap = fetchEmoticonMap()
            val items = diaryRepository
                .getDiariesByYearMonth(year, month)
                .map { mapToItem(it, emoticonMap) }
            _uiState.update { state ->
                state.copy(monthlyDiaries = sortItems(items, state.sortOrder))
            }
        }
    }

    private fun loadAllDiaries() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.update { it.copy(isLoadingAll = true, allDiaries = emptyList()) }
            val totalCount = diaryRepository.getAllDiariesCount()
            _uiState.update { it.copy(allDiariesTotalCount = totalCount) }
            loadAllDiariesPageInternal(offset = 0)
            _uiState.update { it.copy(isLoadingAll = false) }
        }
    }

    private fun loadAllDiariesPage(offset: Int) {
        viewModelScope.launch(exceptionHandler) {
            loadAllDiariesPageInternal(offset)
        }
    }

    private suspend fun loadAllDiariesPageInternal(offset: Int) {
        val ascending = _uiState.value.sortOrder == DiarySortOrder.OLDEST
        val emoticonMap = fetchEmoticonMap()
        val newItems = diaryRepository
            .getAllDiariesPaged(offset, PAGE_SIZE, ascending)
            .map { mapToItem(it, emoticonMap) }
        _uiState.update { it.copy(allDiaries = it.allDiaries + newItems) }
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

    private suspend fun fetchEmoticonMap(): Map<Int, Emoticon> =
        emoticonRepository.getEmoticons()
            .getOrElse { emptyList() }
            .associateBy { it.id }

    private fun mapToItem(diary: DiaryEntity, emoticonMap: Map<Int, Emoticon>): DiaryMonthlyItem =
        DiaryMonthlyItem(
            id = diary.id,
            year = diary.year,
            month = diary.month,
            day = diary.day,
            content = diary.content,
            emoticonImage = diary.emoticonId
                ?.toInt()
                ?.let { emoticonMap[it]?.image },
        )

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
