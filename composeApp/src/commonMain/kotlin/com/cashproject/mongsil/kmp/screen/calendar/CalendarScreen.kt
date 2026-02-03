package com.cashproject.mongsil.kmp.screen.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.component.VerticalSpacer
import com.cashproject.mongsil.kmp.screen.calendar.component.CalendarMonthContent
import com.cashproject.mongsil.kmp.screen.calendar.component.CalendarToolbar
import com.cashproject.mongsil.kmp.screen.calendar.component.SimpleCalendarTitle
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
import com.cashproject.mongsil.kmp.screen.calendar.utils.calculateYearMonth
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import kotlin.time.ExperimentalTime

/**
 * 캘린더 메인 화면
 */
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

/**
 * 캘린더 화면 컨텐츠
 */
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

    // ���재 보이는 월 계산
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

            // HorizontalPager로 캘린더 구현
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
