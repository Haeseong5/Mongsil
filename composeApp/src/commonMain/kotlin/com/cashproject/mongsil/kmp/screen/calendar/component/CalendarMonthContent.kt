package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarRecord
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
import com.cashproject.mongsil.kmp.screen.calendar.model.EmoticonData
import com.cashproject.mongsil.kmp.screen.calendar.utils.getDaysInMonth
import com.cashproject.mongsil.kmp.screen.calendar.utils.getStartDayOfWeek
import kotlinx.datetime.LocalDate

/**
 * 특정 월의 캘린더 컨텐츠
 * 요일 헤더와 날짜 그리드를 포함합니다.
 * 
 * @param year 연도
 * @param month 월
 * @param today 오늘 날짜
 * @param uiState 캘린더 UI 상태
 * @param onDateClick 날짜 클릭 콜백
 */
@Composable
fun CalendarMonthContent(
    year: Int,
    month: Int,
    today: LocalDate,
    uiState: CalendarUiState,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // 요일 헤더
        DaysOfWeekTitle()

        // 캘린더 그리드 데이터 생성
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

        // 캘린더 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = false
        ) {
            items(calendarDays.size) { index ->
                val date = calendarDays[index]
                if (date != null) {
                    Box(contentAlignment = Alignment.Center) {
                        CalendarDay(
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

                        // 오늘 날짜 뱃지
                        if (date == today) {
                            NotificationBadge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(1.dp)
                            )
                        }
                    }
                } else {
                    // 빈 셀
                    Box(modifier = Modifier.aspectRatio(1f))
                }
            }
        }
    }
}

// ========== Preview ==========

@Preview(showBackground = true)
@Composable
internal fun CalendarMonthContentPreview() {
    MongsilTheme {
        CalendarMonthContent(
            year = 2026,
            month = 2,
            today = LocalDate(2026, 2, 3),
            uiState = CalendarUiState(
                currentYear = 2026,
                currentMonth = 2,
                calendarRecords = listOf(
                    CalendarRecord(
                        date = LocalDate(2026, 2, 1),
                        emotionId = 1
                    ),
                    CalendarRecord(
                        date = LocalDate(2026, 2, 14),
                        emotionId = 2
                    ),
                    CalendarRecord(
                        date = LocalDate(2026, 2, 20),
                        emotionId = 3
                    )
                ),
                emoticons = listOf(
                    EmoticonData(
                        id = 1,
                        title = "기쁨",
                        imageUrl = "https://example.com/happy.png",
                        textColor = "#000000",
                        backgroundColor = "#FFEB3B"
                    ),
                    EmoticonData(
                        id = 2,
                        title = "사랑",
                        imageUrl = "https://example.com/love.png",
                        textColor = "#FFFFFF",
                        backgroundColor = "#E91E63"
                    ),
                    EmoticonData(
                        id = 3,
                        title = "평온",
                        imageUrl = "https://example.com/calm.png",
                        textColor = "#000000",
                        backgroundColor = "#4CAF50"
                    )
                )
            ),
            onDateClick = {}
        )
    }
}
