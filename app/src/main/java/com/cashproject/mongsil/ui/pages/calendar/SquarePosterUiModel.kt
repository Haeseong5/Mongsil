package com.cashproject.mongsil.ui.pages.calendar

import com.cashproject.mongsil.repository.model.PosterModel
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

data class SquarePosterUiModel(
    val id: String = "",
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val squareImage: String = "",
    val image: String = "",
)

fun List<PosterModel>.toUiModel(calendar: Calendar): List<SquarePosterUiModel> {
    return this.mapIndexed { i, it ->
        if (i != 0) calendar.add(DAY_OF_MONTH, -1)
        SquarePosterUiModel(
            id = it.id,
            year = calendar.get(YEAR),
            month = (calendar.get(MONTH) + 1),
            day = calendar.get(DAY_OF_MONTH),
            squareImage = it.squareImage,
            image = it.image
        )
    }
}