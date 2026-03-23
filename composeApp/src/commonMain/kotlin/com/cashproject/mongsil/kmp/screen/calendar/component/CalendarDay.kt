package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.EmoticonImage
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import com.cashproject.mongsil.kmp.designsystem.extensions.fixedScaleTextStyle
import com.cashproject.mongsil.kmp.model.ImageResource
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.emoticon_01

/**
 * 캘린더 개별 날짜 셀
 *
 * @param date 날짜
 * @param isToday 오늘 날짜 여부
 * @param isRecord 일기 기록 여부
 * @param emoticonImage 이모티콘 이미지 리소스
 * @param onClick 날짜 클릭 콜백
 */
@Composable
fun BoxScope.CalendarDay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    isToday: Boolean,
    isRecord: Boolean,
    emoticonImage: ImageResource?,
    isFuture: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(vertical = 6.dp)
            .size(36.dp)
            .clip(shape = CircleShape)
            .aspectRatio(1f)
            .align(Alignment.Center)
            .alpha(if (isFuture) 0.3f else 1f)
            .circularRippleClickable(
                enabled = !isFuture,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // 날짜 텍스트 (기록이 없을 때만 표시)
        if (!isRecord || emoticonImage == null) {
            Text(
                text = date.dayOfMonth.toString(),
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                style = fixedScaleTextStyle(MongsilTheme.typography.default),
                color = when (date.dayOfWeek) {
                    DayOfWeek.SUNDAY -> MongsilTheme.colorScheme.fillRed
                    DayOfWeek.SATURDAY -> MongsilTheme.colorScheme.fillBlue
                    else -> MongsilTheme.colorScheme.labelStrong
                }
            )
        }

        // 감정 이모티콘 이미지
        if (emoticonImage != null) {
            EmoticonImage(
                image = emoticonImage,
                contentDescription = "감정 이모티콘",
                modifier = Modifier.size(36.dp),
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
                .background(MongsilTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CalendarDay(
                date = LocalDate(2025, 2, 3),
                isToday = false,
                isRecord = false,
                isFuture = false,
                emoticonImage = null,
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
                .background(MongsilTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CalendarDay(
                date = LocalDate(2025, 2, 3),
                isToday = true,
                isRecord = false,
                isFuture = false,
                emoticonImage = null,
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
                .background(MongsilTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CalendarDay(
                date = LocalDate(2025, 2, 2), // 일요일
                isToday = false,
                isRecord = false,
                isFuture = false,
                emoticonImage = null,
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
                .background(MongsilTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CalendarDay(
                date = LocalDate(2025, 2, 3),
                isToday = false,
                isRecord = true,
                isFuture = false,
                emoticonImage = ImageResource.Local(Res.drawable.emoticon_01),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun CalendarDayFuturePreview() {
    MongsilTheme {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MongsilTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CalendarDay(
                date = LocalDate(2099, 12, 25),
                isToday = false,
                isRecord = false,
                isFuture = true,
                emoticonImage = null,
                onClick = {}
            )
        }
    }
}
