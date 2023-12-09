package com.cashproject.mongsil.ui.compose.screen.calendar.list

import com.cashproject.mongsil.data.repository.model.Poster
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

data class SquarePosterUiModel(
    val year: String,
    val month: String,
    val day: String,
    val squareImage: String,
)

fun List<Poster>.toUiModel(calendar: Calendar): List<SquarePosterUiModel> {
    return this.mapIndexed { i, it ->
        if (i != 0) calendar.add(DAY_OF_MONTH, -1)
        SquarePosterUiModel(
            year = calendar.get(YEAR).toString(),
            month = (calendar.get(MONTH) + 1).toString(),
            day = calendar.get(DAY_OF_MONTH).toString(),
            squareImage = it.squareImage
        )
    }
}