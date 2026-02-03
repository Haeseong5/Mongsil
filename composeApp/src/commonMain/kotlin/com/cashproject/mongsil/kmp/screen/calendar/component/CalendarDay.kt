package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

/**
 * 캘린더 개별 날짜 셀
 * 
 * @param date 날짜
 * @param isToday 오늘 날짜 여부
 * @param isRecord 일기 기록 여부
 * @param emoticonImageUrl 이모티콘 이미지 URL
 * @param onClick 날짜 클릭 콜백
 */
@Composable
fun BoxScope.CalendarDay(
    date: LocalDate,
    isToday: Boolean,
    isRecord: Boolean,
    emoticonImageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
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
        // 날짜 텍스트 (기록이 없을 때만 표시)
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

        // 감정 이모티콘 이미지
        if (emoticonImageUrl.isNotEmpty()) {
            AsyncImage(
                model = emoticonImageUrl,
                contentDescription = "감정 이모티콘",
                modifier = Modifier.size(36.dp),
                onError = { error ->
                    println("이미지 로딩 실패: ${error.result.throwable.message}")
                }
            )
        }
    }
}

// ========== Previews ==========

@Preview(showBackground = true)
@Composable
internal fun CalendarDayPreview() {
    MongsilTheme {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CalendarDay(
                date = LocalDate(2025, 2, 3),
                isToday = false,
                isRecord = false,
                emoticonImageUrl = "",
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun CalendarDayTodayPreview() {
    MongsilTheme {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CalendarDay(
                date = LocalDate(2025, 2, 3),
                isToday = true,
                isRecord = false,
                emoticonImageUrl = "",
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun CalendarDaySundayPreview() {
    MongsilTheme {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CalendarDay(
                date = LocalDate(2025, 2, 2), // 일요일
                isToday = false,
                isRecord = false,
                emoticonImageUrl = "",
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun CalendarDayWithEmoticonPreview() {
    MongsilTheme {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CalendarDay(
                date = LocalDate(2025, 2, 3),
                isToday = false,
                isRecord = true,
                emoticonImageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_01.png?alt=media&token=a58f5622-6568-49a4-9484-90d5cce02316",
                onClick = {}
            )
        }
    }
}
