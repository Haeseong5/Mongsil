package com.cashproject.mongsil.kmp.screen.diarychart.model

import com.cashproject.mongsil.kmp.core.model.TextSource
import com.cashproject.mongsil.kmp.model.ImageResource

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
    val image: ImageResource,
    val title: TextSource,
    val count: Int,
    val barColorHex: String,
)

data class WordCloudItem(
    val word: String,
    val count: Int,
)
