package com.cashproject.mongsil.kmp.screen.diarysearch

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.screen.diarysearch.model.DiarySearchItem
import com.cashproject.mongsil.kmp.screen.diarysearch.model.DiarySearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiarySearchViewModel(
    private val diaryRepository: DiaryRepository,
    private val emoticonRepository: EmoticonRepository,
) : BaseViewModel() {

    private var allItems: List<DiarySearchItem> = emptyList()

    private val _uiState = MutableStateFlow(DiarySearchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDiaryItems()
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        applySearch(query = query)
    }

    private fun loadDiaryItems() {
        viewModelScope.launch(exceptionHandler) {
            val emoticonMap = emoticonRepository
                .getEmoticons()
                .getOrElse { emptyList() }
                .associateBy { it.id }

            allItems = diaryRepository.getAllDiaries().map { diary ->
                DiarySearchItem(
                    id = diary.id,
                    year = diary.year.toInt(),
                    month = diary.month.toInt(),
                    day = diary.day.toInt(),
                    content = diary.content,
                    emoticonImage = diary.emoticonId
                        ?.toInt()
                        ?.let { emoticonMap[it]?.image }
                )
            }

            applySearch(query = _uiState.value.query)
        }
    }

    private fun applySearch(query: String) {
        val normalizedQuery = query.trim()
        val filtered = if (normalizedQuery.isEmpty()) {
            emptyList()
        } else {
            allItems.filter { item ->
                item.content.contains(normalizedQuery, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(results = filtered) }
    }
}
