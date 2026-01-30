package com.cashproject.mongsil.kmp.screen.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime

/**
 * 몽실 캘린더 화면
 * 기존 Android 앱의 디자인을 참고하여 KMP로 구현
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateBack: () -> Unit = {},
    recordedDates: Set<LocalDate> = emptySet(), // 일기가 작성된 날짜들
    onDateClick: (LocalDate) -> Unit = {} // 날짜 클릭 시 일기 작성
) {
    // 오늘 날짜
    val today = remember {
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }

    // 상태 관리
    var selectedDate by remember { mutableStateOf<LocalDate?>(today) }
    var currentYear by remember { mutableIntStateOf(today.year) }
    var currentMonth by remember { mutableIntStateOf(today.monthNumber) }
    var animationDirection by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "📅 몽실 캘린더",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Box(modifier = Modifier.size(24.dp).background(Color.Red))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 월/년도 표시 및 네비게이션
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        animationDirection = -1
                        if (currentMonth == 1) {
                            currentMonth = 12
                            currentYear--
                        } else {
                            currentMonth--
                        }
                    }) {
                        Box(modifier = Modifier.size(24.dp).background(Color.Red))
                    }

                    // 월/년도 표시 with Animation
                    AnimatedContent(
                        targetState = "${currentYear}년 ${currentMonth}월",
                        transitionSpec = {
                            if (animationDirection > 0) {
                                slideInHorizontally { width -> width } + fadeIn() togetherWith
                                        slideOutHorizontally { width -> -width } + fadeOut()
                            } else {
                                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                        slideOutHorizontally { width -> width } + fadeOut()
                            }
                        },
                        label = "month_animation"
                    ) { targetText ->
                        Text(
                            text = targetText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = {
                        animationDirection = 1
                        if (currentMonth == 12) {
                            currentMonth = 1
                            currentYear++
                        } else {
                            currentMonth++
                        }
                    }) {
                        Box(modifier = Modifier.size(24.dp).background(Color.Red))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 요일 헤더
            DaysOfWeekHeader()

            Spacer(modifier = Modifier.height(8.dp))

            // 캘린더 그리드
            val daysInMonth = getDaysInMonth(currentYear, currentMonth)
            val firstDayOfMonth = LocalDate(currentYear, currentMonth, 1)
            val startDayOfWeek = firstDayOfMonth.dayOfWeek.isoDayNumber % 7 // 일요일을 0으로

            val calendarDays = buildList {
                // 이전 달의 빈 칸
                repeat(startDayOfWeek) {
                    add(null)
                }
                // 현재 달의 날짜들
                for (day in 1..daysInMonth) {
                    add(LocalDate(currentYear, currentMonth, day))
                }
            }

            AnimatedContent(
                targetState = "${currentYear}-${currentMonth}",
                transitionSpec = {
                    if (animationDirection > 0) {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                label = "calendar_animation"
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(calendarDays) { date ->
                        if (date != null) {
                            DayCell(
                                date = date,
                                isSelected = selectedDate == date,
                                isToday = today == date,
                                hasRecord = recordedDates.contains(date),
                                onClick = {
                                    selectedDate = it
                                    onDateClick(it)
                                }
                            )
                        } else {
                            Box(modifier = Modifier.aspectRatio(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 선택된 날짜 정보
            if (selectedDate != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "선택된 날짜",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                                        alpha = 0.7f
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${selectedDate!!.year}년 ${selectedDate!!.monthNumber}월 ${selectedDate!!.dayOfMonth}일",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }

                            // 상태 표시
                            if (recordedDates.contains(selectedDate)) {
                                Text(
                                    text = "📝 기록됨",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        if (selectedDate == today) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "오늘 📌",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 통계 정보
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem("전체 기록", recordedDates.size.toString())
                    StatItem(
                        "이번 달",
                        recordedDates.count {
                            it.year == currentYear && it.monthNumber == currentMonth
                        }.toString()
                    )
                }
            }
        }
    }
}

/**
 * 통계 아이템
 */
@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
        )
    }
}

/**
 * 요일 헤더 (일, 월, 화, 수, 목, 금, 토)
 */
@Composable
private fun DaysOfWeekHeader() {
    val weekDayNames = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDayNames.forEachIndexed { index, name ->
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = when (index) {
                    0 -> Color(0xFFE57373) // 일요일 - 밝은 빨강
                    6 -> Color(0xFF64B5F6) // 토요일 - 밝은 파랑
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

/**
 * 날짜 셀 (개선된 버전)
 */
@Composable
private fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasRecord: Boolean,
    onClick: (LocalDate) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        isToday -> MaterialTheme.colorScheme.primaryContainer
                        else -> Color.Transparent
                    }
                )
                .border(
                    width = if (hasRecord && !isSelected) 2.dp else 0.dp,
                    color = if (hasRecord && !isSelected)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    else Color.Transparent,
                    shape = CircleShape
                )
                .clickable { onClick(date) },
            contentAlignment = Alignment.Center
        ) {
            // 날짜 텍스트
            Text(
                text = date.dayOfMonth.toString(),
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    isToday -> MaterialTheme.colorScheme.primary
                    date.dayOfWeek == DayOfWeek.SUNDAY -> Color(0xFFE57373)
                    date.dayOfWeek == DayOfWeek.SATURDAY -> Color(0xFF64B5F6)
                    else -> MaterialTheme.colorScheme.onSurface
                },
                fontSize = 14.sp,
                fontWeight = when {
                    isSelected || isToday || hasRecord -> FontWeight.Bold
                    else -> FontWeight.Normal
                }
            )
        }

        // 오늘 표시 (작은 닷)
        if (isToday && !isSelected) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomCenter)
            )
        }

        // 기록 있음 표시 (작은 닷)
        if (hasRecord && !isSelected && !isToday) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

/**
 * 해당 월의 일 수 계산
 */
private fun getDaysInMonth(year: Int, month: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 30
    }
}

/**
 * 윤년 체크
 */
private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}
