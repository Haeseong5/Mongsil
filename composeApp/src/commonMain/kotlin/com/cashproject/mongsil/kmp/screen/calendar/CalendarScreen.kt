package com.cashproject.mongsil.kmp.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.BannerAdView
import com.cashproject.mongsil.kmp.designsystem.component.ObserveErrorEffect
import com.cashproject.mongsil.kmp.designsystem.component.VerticalSpacer
import com.cashproject.mongsil.kmp.model.Emoticon
import com.cashproject.mongsil.kmp.model.ImageResource
import com.cashproject.mongsil.kmp.screen.calendar.component.CalendarDay
import com.cashproject.mongsil.kmp.screen.calendar.component.CalendarToolbar
import com.cashproject.mongsil.kmp.screen.calendar.component.DayPickerDialog
import com.cashproject.mongsil.kmp.screen.calendar.component.DaysOfWeekTitle
import com.cashproject.mongsil.kmp.screen.calendar.component.NotificationBadge
import com.cashproject.mongsil.kmp.screen.calendar.component.SimpleCalendarYearMonth
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarRecord
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiEvent
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.plusMonths
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.emoticon_01
import mongsil.composeapp.generated.resources.today
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.time.ExperimentalTime
import com.kizitonwose.calendar.core.CalendarDay as KCalendarDay

/**
 * 캘린더 메인 화면
 */
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    viewModel: CalendarViewModel = koinInject(),
    onNavigateToDiaryWrite: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> },
    onNavigateToSetting: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToChart: (year: Int, month: Int) -> Unit = { _, _ -> },
    onNavigateToMonthly: (year: Int, month: Int) -> Unit = { _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveErrorEffect(viewModel.errorEvent)

    CalendarScreenContent(
        modifier = modifier.padding(padding),
        uiState = uiState,
        uiEvent = viewModel::onEvent,
        onDateClick = { date ->
            onNavigateToDiaryWrite(date.year, date.monthNumber, date.dayOfMonth)
        },
        onYearMonthChange = viewModel::updateYearMonth,
        onNavigateToSetting = onNavigateToSetting,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToChart = {
            onNavigateToChart(uiState.currentYear, uiState.currentMonth)
        },
        onNavigateToMonthly = {
            onNavigateToMonthly(uiState.currentYear, uiState.currentMonth)
        }
    )

    if (uiState.isShownYearMonthPicker) {
        DayPickerDialog(
            initialYear = uiState.currentYear,
            onDismissRequest = {
                viewModel.onEvent(CalendarUiEvent.ShowAndHideYearMonthPicker(false))
            },
            onMonthSelected = { year, month ->
                viewModel.onEvent(
                    CalendarUiEvent.OnYearMonthPickerSelected(year = year, month = month)
                )
            }
        )
    }
}

/**
 * 캘린더 화면 컨텐츠
 */
@OptIn(ExperimentalTime::class)
@Composable
fun CalendarScreenContent(
    modifier: Modifier = Modifier,
    uiState: CalendarUiState,
    uiEvent: (CalendarUiEvent) -> Unit = {},
    onDateClick: (LocalDate) -> Unit = {},
    onYearMonthChange: (year: Int, month: Int) -> Unit = { _, _ -> },
    onNavigateToSetting: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToChart: () -> Unit = {},
    onNavigateToMonthly: () -> Unit = {},
) {
    val today = uiState.today

    val currentMonth = remember { YearMonth(today.year, today.month) }
    val startMonth = remember { currentMonth.minusMonths(1200) }
    val endMonth = remember { currentMonth.plusMonths(1200) }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.SUNDAY,
        outDateStyle = OutDateStyle.EndOfGrid,
    )

    val recordMap = remember(uiState.calendarRecords) {
        uiState.calendarRecords.associateBy { it.date }
    }
    val emoticonMap = remember(uiState.emoticons) {
        uiState.emoticons.associateBy { it.id }
    }

    val visibleYearMonth by remember {
        derivedStateOf { calendarState.firstVisibleMonth.yearMonth }
    }
    val coroutineScope = rememberCoroutineScope()
    val isCurrentMonthVisible by remember(visibleYearMonth, today) {
        derivedStateOf {
            visibleYearMonth.year == today.year &&
                    visibleYearMonth.month.number == today.month.number
        }
    }

    // 월 변경 감지 → ViewModel 업데이트 (일기 데이터 로드)
    LaunchedEffect(calendarState) {
        snapshotFlow { calendarState.firstVisibleMonth.yearMonth }.collect { yearMonth ->
            onYearMonthChange(yearMonth.year, yearMonth.month.number)
        }
    }

    // DayPickerDialog에서 년월 선택 시 캘린더 이동
    LaunchedEffect(uiState.scrollTarget) {
        val target = uiState.scrollTarget ?: return@LaunchedEffect
        val targetYearMonth = YearMonth(target.year, Month.entries[target.month - 1])
        val current = calendarState.firstVisibleMonth.yearMonth
        val isAdjacent =
            target.year == current.year && kotlin.math.abs(target.month - current.month.number) == 1
        if (isAdjacent) {
            calendarState.animateScrollToMonth(targetYearMonth)
        } else {
            calendarState.scrollToMonth(targetYearMonth)
        }
        uiEvent(CalendarUiEvent.ClearScrollTarget)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MongsilTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            CalendarToolbar(
                onNavigateToSetting = onNavigateToSetting,
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToChart = onNavigateToChart,
                onNavigateToMonthly = onNavigateToMonthly
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                SimpleCalendarYearMonth(
                    modifier = Modifier
                        .padding(start = 20.dp, bottom = 12.dp),
                    year = visibleYearMonth.year,
                    month = visibleYearMonth.month.number,
                    onClick = {
                        uiEvent.invoke(CalendarUiEvent.ShowAndHideYearMonthPicker(true))
                    }
                )
                VerticalSpacer(16.dp)

                HorizontalCalendar(
                    state = calendarState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    monthHeader = { DaysOfWeekTitle() },
                    dayContent = { day: KCalendarDay ->
                        if (day.position == DayPosition.MonthDate) {
                            val record = recordMap[day.date]
                            Box(
                                modifier = Modifier.align(alignment = Alignment.Center),
                                contentAlignment = Alignment.Center
                            ) {
                                CalendarDay(
                                    date = day.date,
                                    isToday = day.date == today,
                                    isRecord = record != null,
                                    emoticonImage = emoticonMap[record?.emotionId]?.image,
                                    isFuture = day.date > today,
                                    isEmoticonTranslucent = uiState.isEmoticonTranslucent,
                                    onClick = { onDateClick(day.date) }
                                )
                                if (day.date == today) {
                                    NotificationBadge(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(1.dp)
                                    )
                                }
                            }
                        } else {
                            // InDate / OutDate: 빈 셀 (OutDateStyle.EndOfGrid로 항상 6행 유지)
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                                    .size(36.dp)
                            )
                        }
                    }
                )
            }


            Column(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
                if (!isCurrentMonthVisible) {
                    TodayNavigationButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(),
                        onClick = {
                            coroutineScope.launch {
                                calendarState.animateScrollToMonth(currentMonth)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.size(24.dp))
                BannerAdView()
            }


        }
    }
}

@Composable
fun TodayNavigationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(30.dp)
    Card(
        shape = shape,
        colors = CardDefaults.cardColors().copy(containerColor = MongsilTheme.colorScheme.card),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .clickable {
                    onClick.invoke()
                }
                .padding(horizontal = 8.dp, vertical = 6.dp),
            text = stringResource(Res.string.today),
            style = MongsilTheme.typography.label1,
            color = MongsilTheme.colorScheme.labelStrong
        )
    }
}

// ========== Previews ==========

private val previewUiStateEmpty = CalendarUiState(
    today = LocalDate(2026, 3, 8),
    currentYear = 2026,
    currentMonth = 3,
    calendarRecords = emptyList(),
    emoticons = emptyList(),
)

private val previewEmoticons = listOf(
    Emoticon(
        id = 1,
        title = "행복",
        image = ImageResource.Local(Res.drawable.emoticon_01),
        textColor = "#000000",
        backgroundColor = "#FFFFFF"
    ),
    Emoticon(
        id = 2,
        title = "슬픔",
        image = ImageResource.Local(Res.drawable.emoticon_01),
        textColor = "#000000",
        backgroundColor = "#FFFFFF"
    ),
)

private val previewUiStateWithRecords = CalendarUiState(
    today = LocalDate(2026, 3, 8),
    currentYear = 2026,
    currentMonth = 2,
    calendarRecords = listOf(
        CalendarRecord(date = LocalDate(2026, 3, 1), emotionId = 1),
        CalendarRecord(date = LocalDate(2026, 3, 5), emotionId = 2),
        CalendarRecord(date = LocalDate(2026, 3, 8), emotionId = 1),
    ),
    emoticons = previewEmoticons,
)

@Preview(showBackground = true)
@Composable
internal fun CalendarScreenContentEmptyPreview() {
    MongsilTheme {
        CalendarScreenContent(uiState = previewUiStateEmpty)
    }
}

@Preview(showBackground = true)
@Composable
internal fun CalendarScreenContentWithRecordsPreview() {
    MongsilTheme {
        CalendarScreenContent(uiState = previewUiStateWithRecords)
    }
}
