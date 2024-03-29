package com.cashproject.mongsil.ui.pages.calendar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.R
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.ui.model.Emoticons
import com.cashproject.mongsil.ui.theme.dpToSp
import com.cashproject.mongsil.ui.theme.primaryBackgroundColor
import com.cashproject.mongsil.ui.theme.primaryTextColor
import com.cashproject.mongsil.ui.theme.primaryTextStyle
import com.cashproject.mongsil.ui.theme.pxToDp
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onStartDiary: (LocalDate, Poster?) -> Unit,
    visibleCalendarScreenType: CalendarScreenType,
    selectedLastPosterIndex: Int,
    setSelectedLastPosterIndex: (Int) -> Unit,
    onClickFloating: (CalendarScreenType) -> Unit = {},
) {
    val listState = rememberLazyListState(selectedLastPosterIndex)

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
                        setSelectedLastPosterIndex.invoke(index)
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
                        emoticonId = emoticonId,
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


@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = primaryTextStyle,
                color = primaryTextColor
            )
        }
    }
}

@Composable
fun BoxScope.Day(
    day: CalendarDay,
    isRecord: Boolean = false,
    emoticonId: Int?,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .size(36.dp)
            .clip(shape = CircleShape)
            .aspectRatio(1f)
            .align(Alignment.Center)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                when (day.position) {
                    DayPosition.MonthDate -> onClick.invoke()
                    else -> {}
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.alpha(
                when (day.position) {
                    DayPosition.MonthDate -> 1.0f
                    else -> 0.4f
                }
            ),
            text = if (isRecord) "" else day.date.dayOfMonth.toString(),
            fontWeight = if (isRecord || isToday(day)) FontWeight(700) else FontWeight(400),
            style = primaryTextStyle,
            fontSize = dpToSp(dp = 14.dp),
            color = when (day.date.dayOfWeek) {
                DayOfWeek.SUNDAY -> Color(context.getColor(R.color.sunday))
                DayOfWeek.SATURDAY -> Color(context.getColor(R.color.saturday))
                else -> primaryTextColor
            }
        )

        if (emoticonId != null) {
            Image(
                modifier = Modifier.alpha(
                    when (day.position) {
                        DayPosition.MonthDate -> 1.0f
                        else -> 0.4f
                    }
                ),
                painter = painterResource(id = Emoticons.emoticons[emoticonId].icon),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun NotificationBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(
                elevation = (1.5).dp,
                shape = CircleShape
            )
            .clip(shape = CircleShape)
            .size(9.dp)
            .background(color = Color.Red)
    ) { }
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