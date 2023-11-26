package com.cashproject.mongsil.ui.composables.screen.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.R
import com.cashproject.mongsil.ui.theme.dpToSp
import com.cashproject.mongsil.ui.theme.latoTextStyle

@Composable
fun CalendarListScreen() {
    CalendarListScreenContent()

}

@Composable
fun CalendarListScreenContent() {

}

@Composable
fun CalendarDayList() {
    LazyColumn(content = {
//        items() {
//
//        }
    })
}

@Composable
fun CalendarDay(url: String = "") {
    Box {
        Image(
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = ""
        )
        Row {
            Text(
                text = "30",
                fontSize = dpToSp(dp = 30.dp),
                style = latoTextStyle,
                color = Color.White
            )
            Column {
                Text(
                    text = "2023",
                    style = latoTextStyle,
                    color = Color.White
                )
                Text(
                    text = "October",
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
    CalendarDay()
//    CalendarListScreenContent()
}