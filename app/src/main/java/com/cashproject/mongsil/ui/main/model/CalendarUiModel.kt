package com.cashproject.mongsil.ui.main.model

import java.time.LocalDate

data class CalendarUiModel(
    val date : LocalDate = LocalDate.now(),
    val emotionId: Int,
)
