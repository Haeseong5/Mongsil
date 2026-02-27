package com.cashproject.mongsil.kmp.screen.diarychart.model

data class DiaryChartUiState(
    val year: Int,
    val month: Int,
    val items: List<DiaryChartItem> = emptyList(),
    val canMoveNextMonth: Boolean = true,
)

data class DiaryChartItem(
    val emoticonId: Int,
    val imageUrl: String,
    val count: Int,
    val barColorHex: String,
)
