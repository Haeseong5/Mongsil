package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme

/**
 * 오늘 날짜 표시 뱃지
 * 캘린더에서 오늘 날짜 위에 빨간 점으로 표시됩니다.
 */
@Composable
fun NotificationBadge(
    modifier: Modifier = Modifier
) {
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

// ========== Preview ==========

@Preview(showBackground = true)
@Composable
internal fun NotificationBadgePreview() {
    MongsilTheme {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White),
            contentAlignment = Alignment.TopEnd
        ) {
            NotificationBadge(
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
