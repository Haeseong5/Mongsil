package com.cashproject.mongsil.ui.composables.screen.calendar.list


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cashproject.mongsil.ui.theme.dpToSp
import com.cashproject.mongsil.ui.theme.latoTextStyle
import java.util.Calendar.DATE
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

@Composable
fun CalendarListScreen(
    viewModel: CalendarListViewModel = viewModel()
) {
    val posters by viewModel.posters.collectAsState()
    CalendarListScreenContent(
        posters = posters
    )

}

@Composable
fun CalendarListScreenContent(
    posters: List<SquarePosterUiModel> = emptyList()
) {
    CalendarDayList(
        posters = posters
    )
}

@Composable
fun CalendarDayList(
    posters: List<SquarePosterUiModel> = emptyList()
) {
    Log.d("++##", "calendars ${posters.size}")
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(posters.size) {
                CalendarDay(posters[it])
            }
        })
}

@Composable
fun CalendarDay(
    poster: SquarePosterUiModel,
) {
    Box {
        AsyncImage(
            model = poster.squareImage,
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(6.dp),
                text = poster.day,
                fontSize = dpToSp(dp = 30.dp),
                style = latoTextStyle,
                color = Color.White
            )
            Column() {
                Text(
                    text = poster.month,
                    fontSize = dpToSp(dp = 14.dp),
                    style = latoTextStyle,
                    color = Color.White
                )
                Text(
                    text = poster.year,
                    fontSize = dpToSp(dp = 14.dp),
                    style = latoTextStyle,
                    color = Color.White
                )

            }
        }

    }
}

@Preview
@Composable
fun PreviewCalendarListScreen() {
    CalendarListScreenContent(
        posters = listOf(
            SquarePosterUiModel(
                year = "2023",
                month = "5",
                day = "5",
                squareImage = ""
            ),
            SquarePosterUiModel(
                year = "2023",
                month = "5",
                day = "10",
                squareImage = ""
            ),
            SquarePosterUiModel(
                year = "2023",
                month = "5",
                day = "1",
                squareImage = ""
            )
        )
    )
}