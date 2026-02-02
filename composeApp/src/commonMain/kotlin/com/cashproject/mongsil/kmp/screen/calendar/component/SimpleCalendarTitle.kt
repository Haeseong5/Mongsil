package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.HorizontalSpacer
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.ic_baseline_arrow_forward_ios_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier = Modifier,
    year: Int,
    month: Int,
    goToPrevious: () -> Unit = {},
    goToNext: () -> Unit = {}
) {
    Row(
        modifier = modifier.height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이전 달 버튼
        Icon(
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    goToPrevious()
                },
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "back",
            tint = Color.Black
        )
        HorizontalSpacer(30.dp) // TODO 좌우 여백 조정 필요
        Text(
            modifier = Modifier.width(100.dp), // TODO 최적의 width 지정 필요
            text = "${year}년 ${month}월",
            textAlign = TextAlign.Center,
            style = MongsilTheme.typography.label1
        )
        HorizontalSpacer(30.dp)
        Icon(
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    goToNext()
                },
            painter = painterResource(Res.drawable.ic_baseline_arrow_forward_ios_24),
            contentDescription = "forward",
            tint = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleCalendarTitlePreview() {
    MongsilTheme {
        SimpleCalendarTitle(
            year = 2026,
            month = 2,
            goToPrevious = {},
            goToNext = {}
        )
    }
}