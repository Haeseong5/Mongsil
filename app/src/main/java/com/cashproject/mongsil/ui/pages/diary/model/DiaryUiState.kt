package com.cashproject.mongsil.ui.pages.diary.model

import com.cashproject.mongsil.repository.model.DailyEmoticon
import com.cashproject.mongsil.ui.model.Emoticon
import java.util.Date

data class DiaryUiState(
    val date: Date = Date(),
    val poster: Poster = Poster("", "", ""),
    val comments: List<Comment> = emptyList(),
    val emoticonId: Int = 1,
    val inputText: String = "",
    val isPagerItem: Boolean = false,
    val dailyEmoticon: DailyEmoticon = DailyEmoticon(),
    val emoticons: List<Emoticon> = emptyList(),
)