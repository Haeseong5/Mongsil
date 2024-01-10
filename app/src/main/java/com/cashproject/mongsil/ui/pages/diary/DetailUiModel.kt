package com.cashproject.mongsil.ui.pages.diary

import java.util.Date

data class DetailUiModel(
    val id: Int = 0,
    val content: String = "",
    val emotion: Int = 0,
    val time: Date = Date(), //작성 시간. 날짜 고려 X
    val date: Date = Date(), //어떤 날짜에 작성했는 지. 시간 고려 X
)