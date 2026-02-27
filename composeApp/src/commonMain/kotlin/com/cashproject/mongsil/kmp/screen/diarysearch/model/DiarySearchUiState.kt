package com.cashproject.mongsil.kmp.screen.diarysearch.model

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
)
