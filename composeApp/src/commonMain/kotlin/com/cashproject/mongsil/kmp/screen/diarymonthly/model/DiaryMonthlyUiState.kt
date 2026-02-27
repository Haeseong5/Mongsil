package com.cashproject.mongsil.kmp.screen.diarymonthly.model

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
)

enum class DiarySortOrder {
    LATEST,
    OLDEST,
}
