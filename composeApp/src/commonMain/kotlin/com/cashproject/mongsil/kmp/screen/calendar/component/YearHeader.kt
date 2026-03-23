package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.ic_baseline_arrow_forward_ios_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun YearHeader(
    modifier: Modifier = Modifier,
    year: Int,
    goToPrevious: () -> Unit = {},
    goToNext: () -> Unit = {}
) {
    val iconSize = 12.dp
    Row(
        modifier = modifier.height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이전 달 버튼
        Icon(
            modifier = Modifier
                .size(iconSize)
                .circularRippleClickable(radius = 18.dp) {
                    goToPrevious()
                },
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "back",
            tint = MongsilTheme.colorScheme.labelStrong
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${year}년",
            textAlign = TextAlign.Center,
            style = MongsilTheme.typography.title3,
            color = MongsilTheme.colorScheme.labelStrong
        )
        Icon(
            modifier = Modifier
                .size(iconSize)
                .circularRippleClickable(radius = 18.dp) {
                    goToNext()
                },
            painter = painterResource(Res.drawable.ic_baseline_arrow_forward_ios_24),
            contentDescription = "forward",
            tint = MongsilTheme.colorScheme.labelStrong
        )
    }
}

@Preview(showBackground = true)
@Composable
fun YearHeaderPreview() {
    MongsilTheme {
        YearHeader(
            year = 2026,
            goToPrevious = {},
            goToNext = {}
        )
    }
}