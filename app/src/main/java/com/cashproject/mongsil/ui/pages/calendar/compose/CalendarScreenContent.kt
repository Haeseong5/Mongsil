package com.cashproject.mongsil.ui.pages.calendar.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.ui.pages.calendar.CalendarUiState
import com.cashproject.mongsil.ui.theme.primaryBackgroundColor
import com.cashproject.mongsil.ui.theme.pxToDp
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreenContent(
    uiState: CalendarUiState = CalendarUiState(),
    onClickDay: (LocalDate) -> Unit = {},
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(50) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(50) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    val daysOfWeek = remember { daysOfWeek() }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberFirstMostVisibleMonth(state, viewportPercent = 90f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = primaryBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
        ) {
            SimpleCalendarTitle(
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .align(Alignment.CenterHorizontally),
                currentMonth = visibleMonth.yearMonth,
                goToPrevious = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                    }
                },
                goToNext = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                    }
                },
            )


            HorizontalCalendar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(pxToDp(pixels = 863f)),
                state = state,
                dayContent = { day ->
                    val emoticonId =
                        uiState.calendarUiModel.lastOrNull { it.date == day.date }?.emotionId
                    Day(
                        day = day,
                        isRecord = uiState.calendarUiModel.any { it.date == day.date },
                        emoticonImageUrl = uiState.emoticons.find { it.id == emoticonId }?.imageUrl
                            ?: "",
                        onClick = {
                            onClickDay.invoke(day.date)
                        }
                    )
                    if (isToday(day)) {
                        NotificationBadge(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(1.dp)
                        )
                    }
                },
                monthHeader = {
                    DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                }
            )
        }
    }
}

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}

@Composable
fun rememberFirstMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 50f,
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

fun isToday(day: CalendarDay): Boolean {
    val today = LocalDate.now()
    return day.date.isEqual(today)
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun PreviewCalendarScreenLightMode(@PreviewParameter(SampleLoaderProvider::class) uiState: CalendarUiState) {
    CalendarScreenContent()
}

@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewCalendarScreenDarkMode(@PreviewParameter(SampleLoaderProvider::class) uiState: CalendarUiState) {
    CalendarScreenContent()
}

class SampleLoaderProvider : PreviewParameterProvider<CalendarUiState> {
    override val values: Sequence<CalendarUiState> = sequenceOf(
        CalendarUiState(),
    )
}