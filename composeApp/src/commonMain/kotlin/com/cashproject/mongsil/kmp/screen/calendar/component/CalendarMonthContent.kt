package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.screen.calendar.model.CalendarUiState
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
    // calendarRecords를 Map으로 변환 — O(n²) → O(1) 탐색
    val recordMap = remember(uiState.calendarRecords) {
        uiState.calendarRecords.associateBy { it.date }
    }
    val emoticonMap = remember(uiState.emoticons) {
        uiState.emoticons.associateBy { it.id }
    }

    Column(modifier = modifier) {
        // 요일 헤더
        DaysOfWeekTitle()

        // 캘린더 그리드 데이터 생성
        val daysInMonth = getDaysInMonth(year, month)
        val firstDayOfMonth = LocalDate(year, month, 1)
        val startDayOfWeek = getStartDayOfWeek(firstDayOfMonth)

        val calendarDays = remember(year, month) {
            buildList {
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
        }

        // 캘린더 그리드
        // 항상 6행 × 48dp(size 36dp + vertical padding 12dp) = 288dp 고정 높이 유지
        // → 어떤 달이든 Column 높이가 일정해 헤더가 움직이지 않음
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(288.dp),
            userScrollEnabled = false
        ) {
            items(calendarDays.size) { index ->
                val date = calendarDays[index]
                if (date != null) {
                    val record = recordMap[date]
                    Box(contentAlignment = Alignment.Center) {
                        CalendarDay(
                            date = date,
                            isToday = date == today,
                            isRecord = record != null,
                            emoticonImageUrl = emoticonMap[record?.emotionId]?.imageUrl ?: "",
                            isFuture = date > today,
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
                    // 빈 셀 – 실제 날짜 셀과 동일한 높이를 유지해 행 높이를 통일
                    Box(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .size(36.dp)
                    )
                }
            }
        }
    }
}
