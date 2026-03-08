package com.cashproject.mongsil.kmp.screen.diarysearch.model

import com.cashproject.mongsil.kmp.core.data.Date

data class DiarySearchUiState(
    val query: String = "",
    val results: List<DiarySearchItem> = emptyList(),
)

data class DiarySearchItem(
    val id: Long,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String,
    val emoticonImageUrl: String,
) {
    val date: Date = Date(year, month, day)
}
