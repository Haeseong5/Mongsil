package com.cashproject.mongsil.kmp.screen.diarymonthly.model

import com.cashproject.mongsil.kmp.core.data.Date

data class DiaryMonthlyUiState(
    val year: Int,
    val month: Int,
    val sortOrder: DiarySortOrder = DiarySortOrder.LATEST,
    val diaries: List<DiaryMonthlyItem> = emptyList(),
    val canMoveNextMonth: Boolean = true,
)

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
