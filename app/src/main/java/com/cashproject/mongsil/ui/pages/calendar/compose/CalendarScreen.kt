package com.cashproject.mongsil.ui.pages.calendar.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.R
import com.cashproject.mongsil.ui.pages.calendar.CalendarListScreen
import com.cashproject.mongsil.ui.pages.calendar.CalendarScreenType
import com.cashproject.mongsil.ui.pages.calendar.CalendarUiState
import com.cashproject.mongsil.ui.pages.diary.model.Poster
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onStartDiary: (LocalDate, Poster?) -> Unit,
    visibleCalendarScreenType: CalendarScreenType,
    onClickFloating: (CalendarScreenType) -> Unit = {},
) {
    val listState = rememberLazyListState()

    Box {
        when (visibleCalendarScreenType) {
            CalendarScreenType.DEFAULT -> {
                CalendarScreenContent(
                    uiState = uiState,
                    onClickDay = {
                        onStartDiary.invoke(it, null)
                    }
                )
            }

            CalendarScreenType.LIST -> {
                CalendarListScreen(
                    listState = listState,
                    onClick = { date, poster, index ->
                        onStartDiary.invoke(date, poster)
                    })
            }
        }

        // size ??
        FloatingActionButton(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomEnd),
            containerColor = colorResource(id = R.color.colorYellow),
            contentColor = colorResource(id = R.color.floating_action_btn_color),
            shape = FloatingActionButtonDefaults.largeShape,
            onClick = {
                onClickFloating.invoke(visibleCalendarScreenType)
            },
            content = {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = if (visibleCalendarScreenType == CalendarScreenType.DEFAULT) R.drawable.ic_list else R.drawable.ic_calendar),
                    contentDescription = ""
                )
            })
    }
}