package com.cashproject.mongsil.kmp.screen.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.VerticalSpacer
import com.cashproject.mongsil.kmp.screen.calendar.component.CalendarMonthContent
import com.cashproject.mongsil.kmp.screen.calendar.component.CalendarToolbar
import com.cashproject.mongsil.kmp.screen.calendar.component.DayPickerDialog
import com.cashproject.mongsil.kmp.screen.calendar.component.SimpleCalendarTitleV2
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiEvent
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
import com.cashproject.mongsil.kmp.screen.calendar.utils.calculateYearMonth
import kotlinx.datetime.LocalDate
import org.koin.compose.koinInject
import kotlin.time.ExperimentalTime

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
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalendarScreenContent(
        modifier = modifier.padding(padding),
        uiState = uiState,
        uiEvent = viewModel::onEvent,
        onDateClick = { date ->
            onNavigateToDiaryWrite(date.year, date.monthNumber, date.dayOfMonth)
        },
        onYearMonthChange = viewModel::updateYearMonth,
        onYearMonthPickerSelected = { year, month ->
            viewModel.onEvent(
                CalendarUiEvent.OnYearMonthPickerSelected(year = year, month = month)
            )
        },
        onNavigateToSetting = onNavigateToSetting,
        onNavigateToSearch = onNavigateToSearch
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
@OptIn(ExperimentalFoundationApi::class, ExperimentalTime::class)
@Composable
fun CalendarScreenContent(
    modifier: Modifier = Modifier,
    uiState: CalendarUiState,
    uiEvent: (CalendarUiEvent) -> Unit = {},
    onDateClick: (LocalDate) -> Unit = {},
    onYearMonthChange: (year: Int, month: Int) -> Unit = { _, _ -> },
    onYearMonthPickerSelected: (year: Int, month: Int) -> Unit = { _, _ -> },
    onNavigateToSetting: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
) {
    val today = uiState.today
    val initialMonth = remember { today }
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
            calculateYearMonth(initialMonth.year, initialMonth.monthNumber, offset)
        }
    }

    // 페이지 변경 시 UiState 업데이트
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val offset = page - initialPage
            val (year, month) = calculateYearMonth(
                initialMonth.year,
                initialMonth.monthNumber,
                offset
            )
            onYearMonthChange(year, month)
        }
    }

    // DayPickerDialog에서 년월 선택 시 pager 이동
    LaunchedEffect(uiState.currentYear, uiState.currentMonth) {
        // 초기 로딩이 아닌 경우에만 pager 이동
        if (uiState.currentYear != initialMonth.year || uiState.currentMonth != initialMonth.monthNumber) {
            val yearDiff = uiState.currentYear - initialMonth.year
            val monthDiff = uiState.currentMonth - initialMonth.monthNumber
            val targetOffset = yearDiff * 12 + monthDiff
            val targetPage = initialPage + targetOffset
            
            if (targetPage in 0 until totalPages) {
                pagerState.animateScrollToPage(targetPage)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MongsilTheme.colorScheme.background)
    ) {
        CalendarToolbar(
            onNavigateToSetting = onNavigateToSetting,
            onNavigateToSearch = onNavigateToSearch
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            SimpleCalendarTitleV2(
                modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
                year = visibleYearMonth.first,
                month = visibleYearMonth.second,
                onClick = {
                    uiEvent.invoke(CalendarUiEvent.ShowAndHideYearMonthPicker(true))
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
                    initialMonth.year,
                    initialMonth.monthNumber,
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
