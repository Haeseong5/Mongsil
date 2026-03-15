package com.cashproject.mongsil.kmp.screen.diarychart.model

data class DiaryChartUiState(
    val year: Int,
    val month: Int,
    val items: List<DiaryChartItem> = emptyList(),
    val canMoveNextMonth: Boolean = true,
    val currentStreak: Int = 0,
    val wordCloudItems: List<WordCloudItem> = emptyList(),
)

data class DiaryChartItem(
    val emoticonId: Int,
    val imageUrl: String,
    val title: String,
    val count: Int,
    val barColorHex: String,
)

data class WordCloudItem(
    val word: String,
    val count: Int,
)
