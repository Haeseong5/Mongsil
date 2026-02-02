package com.cashproject.mongsil.kmp.screen.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.IconToolbar
import com.cashproject.mongsil.kmp.designsystem.component.VerticalSpacer
import com.cashproject.mongsil.kmp.screen.calendar.component.CalendarToolbar
import com.cashproject.mongsil.kmp.screen.calendar.component.SimpleCalendarTitle
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_trash
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlin.time.ExperimentalTime

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = koinInject(),
    onNavigateToDiaryWrite: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalendarScreenContent(
        modifier = modifier,
        uiState = uiState,
        onDateClick = { date ->
            onNavigateToDiaryWrite(date.year, date.monthNumber, date.dayOfMonth)
        },
        onYearMonthChange = viewModel::updateYearMonth
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalTime::class)
@Composable
fun CalendarScreenContent(
    modifier: Modifier = Modifier,
    uiState: CalendarUiState,
    onDateClick: (LocalDate) -> Unit = {},
    onYearMonthChange: (year: Int, month: Int) -> Unit = { _, _ -> }
) {
    // 오늘 날짜
    val today: LocalDate = remember {
        val now = Clock.System.now()
        now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    // 현재 월을 기준으로 초기 페이지 설정 (충분히 큰 범위)
    val currentMonth = remember { today }
    val initialPage = 1200 // 중앙 페이지
    val totalPages = 2400 // 전후 100년 정도

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { totalPages }
    )
    val coroutineScope = rememberCoroutineScope()

    // 현재 보이는 월 계산
    val visibleYearMonth by remember {
        derivedStateOf {
            val offset = pagerState.currentPage - initialPage
            calculateYearMonth(currentMonth.year, currentMonth.monthNumber, offset)
        }
    }

    // 페이지 변경 시 UiState 업데이트
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val offset = page - initialPage
            val (year, month) = calculateYearMonth(
                currentMonth.year,
                currentMonth.monthNumber,
                offset
            )
            onYearMonthChange(year, month)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        CalendarToolbar()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            // 월/년도 표시 및 네비게이션
            SimpleCalendarTitle(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                year = visibleYearMonth.first,
                month = visibleYearMonth.second,
                goToPrevious = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                goToNext = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            )
            VerticalSpacer(16.dp)

            // HorizontalPager로 캘린더 구현 (고정된 높이로 위치 안정화)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) { page ->
                val offset = page - initialPage
                val (pageYear, pageMonth) = calculateYearMonth(
                    currentMonth.year,
                    currentMonth.monthNumber,
                    offset
                )

                CalendarMonthContent(
                    year = pageYear,
                    month = pageMonth,
                    today = today,
                    uiState = uiState,
                    onDateClick = onDateClick
                )
            }
        }
    }
}



@Composable
private fun CalendarMonthContent(
    year: Int,
    month: Int,
    today: LocalDate,
    uiState: CalendarUiState,
    onDateClick: (LocalDate) -> Unit
) {
    Column {
        // 요일 헤더
        DaysOfWeekTitle()

        // 캘린더 그리드
        val daysInMonth = getDaysInMonth(year, month)
        val firstDayOfMonth = LocalDate(year, month, 1)
        val startDayOfWeek = getStartDayOfWeek(firstDayOfMonth)

        val calendarDays = buildList {
            // 이전 달의 빈 칸
            repeat(startDayOfWeek) {
                add(null)
            }
            // 현재 달의 날짜들
            for (day in 1..daysInMonth) {
                add(LocalDate(year, month, day))
            }
            // 항상 6주(42칸)를 유지하기 위해 남은 칸 채우기
            val remainingCells = 42 - size
            repeat(remainingCells) {
                add(null)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = false
        ) {
            items(calendarDays.size) { index ->
                val date = calendarDays[index]
                if (date != null) {
                    Box(contentAlignment = Alignment.Center) {
                        Day(
                            date = date,
                            isToday = date == today,
                            isRecord = uiState.calendarRecords.any { it.date == date },
                            emoticonImageUrl = uiState.emoticons
                                .find { emoticon ->
                                    uiState.calendarRecords
                                        .lastOrNull { it.date == date }
                                        ?.emotionId == emoticon.id
                                }?.imageUrl ?: "",
                            onClick = { onDateClick(date) }
                        )

                        if (date == today) {
                            NotificationBadge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(1.dp)
                            )
                        }
                    }
                } else {
                    Box(modifier = Modifier.aspectRatio(1f))
                }
            }
        }
    }
}


@Composable
private fun DaysOfWeekTitle() {
    // TODO 다국어 지원
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = day,
                style = MongsilTheme.typography.body2Normal,
                color = when (index) {
                    0 -> Color(0xFFE57373) // 일요일
                    6 -> Color(0xFF64B5F6) // 토요일
                    else -> Color.Black
                }
            )
        }
    }
}

@Composable
private fun BoxScope.Day(
    date: LocalDate,
    isToday: Boolean,
    isRecord: Boolean,
    emoticonImageUrl: String,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

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
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        // 날짜 텍스트
        if (!isRecord) {
            Text(
                text = date.dayOfMonth.toString(),
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp,
                color = when (date.dayOfWeek) {
                    DayOfWeek.SUNDAY -> Color(0xFFE57373)
                    DayOfWeek.SATURDAY -> Color(0xFF64B5F6)
                    else -> Color.Black
                }
            )
        }

        // 감정 이모티콘 이미지 (향후 Coil 등의 이미지 로더 추가 필요)
        if (emoticonImageUrl.isNotEmpty()) {
            // TODO: AsyncImage로 이모티콘 이미지 표시
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFFFEB3B), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "😊",
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun NotificationBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 1.5.dp,
                shape = CircleShape
            )
            .clip(shape = CircleShape)
            .size(9.dp)
            .background(color = Color.Red)
    )
}

// 유틸리티 함수들

private fun calculateYearMonth(baseYear: Int, baseMonth: Int, offset: Int): Pair<Int, Int> {
    var year = baseYear
    var month = baseMonth + offset

    while (month > 12) {
        month -= 12
        year++
    }
    while (month < 1) {
        month += 12
        year--
    }

    return Pair(year, month)
}

private fun getDaysInMonth(year: Int, month: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 30
    }
}

private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

private fun getStartDayOfWeek(date: LocalDate): Int {
    // 일요일을 0으로 시작
    return when (date.dayOfWeek) {
        DayOfWeek.SUNDAY -> 0
        DayOfWeek.MONDAY -> 1
        DayOfWeek.TUESDAY -> 2
        DayOfWeek.WEDNESDAY -> 3
        DayOfWeek.THURSDAY -> 4
        DayOfWeek.FRIDAY -> 5
        DayOfWeek.SATURDAY -> 6
        else -> 0
    }
}
