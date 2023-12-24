package com.cashproject.mongsil.ui.pages.detail

data class DiaryUiState(
    val posterUrl: String = "",
    val comments: List<Comment> = emptyList(),
    val emoticonId: Int = 0,
    val inputText: String = "",
)