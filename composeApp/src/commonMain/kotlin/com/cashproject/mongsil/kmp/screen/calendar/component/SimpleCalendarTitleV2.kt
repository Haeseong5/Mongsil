package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.layout.Row
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
import com.cashproject.mongsil.kmp.designsystem.component.HorizontalSpacer
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_baseline_arrow_forward_ios_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun SimpleCalendarTitleV2(
    modifier: Modifier = Modifier,
    year: Int,
    month: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier,
            text = "${year}년 ${month}월",
            textAlign = TextAlign.Center,
            style = MongsilTheme.typography.title3,
            color = MongsilTheme.colorScheme.labelStrong
        )
        HorizontalSpacer(8.dp)
        Icon(
            modifier = Modifier
                .size(20.dp),
            painter = painterResource(Res.drawable.ic_baseline_arrow_forward_ios_24),
            contentDescription = "forward",
            tint = MongsilTheme.colorScheme.labelStrong
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleCalendarTitleV2Preview() {
    MongsilTheme {
        SimpleCalendarTitleV2(
            year = 2026,
            month = 2,
        )
    }
}