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
import com.cashproject.mongsil.kmp.designsystem.extensions.fixedScaleTextStyle
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.day_of_week_short_fri
import mongsil.composeapp.generated.resources.day_of_week_short_mon
import mongsil.composeapp.generated.resources.day_of_week_short_sat
import mongsil.composeapp.generated.resources.day_of_week_short_sun
import mongsil.composeapp.generated.resources.day_of_week_short_thu
import mongsil.composeapp.generated.resources.day_of_week_short_tue
import mongsil.composeapp.generated.resources.day_of_week_short_wed
import org.jetbrains.compose.resources.stringResource

/**
 * 캘린더 요일 헤더
 * 일요일부터 토요일까지 표시
 */
@Composable
fun DaysOfWeekTitle(
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf(
        stringResource(Res.string.day_of_week_short_sun),
        stringResource(Res.string.day_of_week_short_mon),
        stringResource(Res.string.day_of_week_short_tue),
        stringResource(Res.string.day_of_week_short_wed),
        stringResource(Res.string.day_of_week_short_thu),
        stringResource(Res.string.day_of_week_short_fri),
        stringResource(Res.string.day_of_week_short_sat),
    )

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
                style = fixedScaleTextStyle(MongsilTheme.typography.body2Normal),
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
