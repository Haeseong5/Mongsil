package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme

/**
 * 캘린더 요일 헤더
 * 일요일부터 토요일까지 표시
 */
@Composable
fun DaysOfWeekTitle(
    modifier: Modifier = Modifier
) {
    // TODO: 다국어 지원
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = modifier
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
                    0 -> MongsilTheme.colorScheme.fillRed // 일요일
                    6 -> MongsilTheme.colorScheme.fillBlue // 토요일
                    else -> MongsilTheme.colorScheme.labelStrong
                }
            )
        }
    }
}

// ========== Preview ==========

@Preview(showBackground = true)
@Composable
internal fun DaysOfWeekTitlePreview() {
    MongsilTheme {
        DaysOfWeekTitle(
            modifier = Modifier.background(MongsilTheme.colorScheme.background)
        )
    }
}
