package com.cashproject.mongsil.ui.pages.calendar


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.pages.diary.model.Poster
import com.cashproject.mongsil.ui.theme.dpToSp
import com.cashproject.mongsil.ui.theme.primaryTextStyle
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarListScreen(
    listState: LazyListState,
    onClick: (LocalDate, Poster, Int) -> Unit,
    viewModel: CalendarListViewModel = viewModel()
) {
    val posters by viewModel.posters.collectAsState()

    CalendarListScreenContent(
        listState = listState,
        posters = posters,
        onClick = {
            val poster = posters[it]
            val date = LocalDate.of(poster.year, poster.month, poster.day)
            val posterInfo =
                Poster(id = poster.id, image = poster.image, squareImage = poster.squareImage)

            onClick.invoke(date, posterInfo, it)
        }
    )
}

@Composable
fun CalendarListScreenContent(
    listState: LazyListState = rememberLazyListState(),
    posters: List<SquarePosterUiModel> = emptyList(),
    onClick: (Int) -> Unit = {}
) {
    CalendarDayList(
        listState = listState,
        posters = posters,
        onClick = onClick
    )
}

@Composable
fun CalendarDayList(
    listState: LazyListState,
    posters: List<SquarePosterUiModel> = emptyList(),
    onClick: (Int) -> Unit,
) {
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(posters.size) {
                CalendarDay(
                    poster = posters[it],
                    onClick = { onClick.invoke(it) }
                )
            }
        })
}

@Composable
fun CalendarDay(
    poster: SquarePosterUiModel,
    onClick: () -> Unit = {}
) {
    Box {
        AsyncImage(
            modifier = Modifier
                .aspectRatio(1f, true)
                .noRippleClickable {
                    onClick.invoke()
                },
            model = poster.squareImage,
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
                text = poster.day.toString(),
                fontSize = dpToSp(dp = 30.dp),
                style = primaryTextStyle,
                color = Color.White
            )
            Column() {
                Text(
                    text = poster.month.toString(),
                    fontSize = dpToSp(dp = 14.dp),
                    style = primaryTextStyle,
                    color = Color.White
                )
                Text(
                    text = poster.year.toString(),
                    fontSize = dpToSp(dp = 14.dp),
                    style = primaryTextStyle,
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
                year = 2023,
                month = 5,
                day = 5,
                squareImage = ""
            ),
            SquarePosterUiModel(
                year = 2023,
                month = 5,
                day = 5,
                squareImage = ""
            ),
            SquarePosterUiModel(
                year = 2023,
                month = 5,
                day = 5,
                squareImage = ""
            ),
        )
    )
}