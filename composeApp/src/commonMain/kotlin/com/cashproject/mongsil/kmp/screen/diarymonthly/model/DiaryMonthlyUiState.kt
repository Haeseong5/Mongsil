package com.cashproject.mongsil.kmp.screen.diarymonthly.model

import com.cashproject.mongsil.kmp.core.data.Date

data class DiaryMonthlyUiState(
    val year: Int,
    val month: Int,
    val viewMode: DiaryViewMode = DiaryViewMode.MONTHLY,
    val sortOrder: DiarySortOrder = DiarySortOrder.LATEST,
    val monthlyDiaries: List<DiaryMonthlyItem> = emptyList(),
    val allDiaries: List<DiaryMonthlyItem> = emptyList(),
    val displayCount: Int = PAGE_SIZE,
    val isLoadingAll: Boolean = false,
    val canMoveNextMonth: Boolean = true,
) {
    val displayedDiaries: List<DiaryMonthlyItem>
        get() = when (viewMode) {
            DiaryViewMode.MONTHLY -> monthlyDiaries
            DiaryViewMode.ALL -> allDiaries.take(displayCount)
        }

    val hasMorePages: Boolean
        get() = viewMode == DiaryViewMode.ALL && displayCount < allDiaries.size

    companion object {
        const val PAGE_SIZE = 20
    }
}

data class DiaryMonthlyItem(
    val id: Long,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String,
    val emoticonImageUrl: String,
) {
    val date: Date = Date(year, month, day)
}

enum class DiarySortOrder {
    LATEST,
    OLDEST,
}

enum class DiaryViewMode {
    MONTHLY,
    ALL,
}
