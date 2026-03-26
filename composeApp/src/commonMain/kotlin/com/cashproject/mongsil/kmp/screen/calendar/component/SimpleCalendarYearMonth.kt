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
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import com.cashproject.mongsil.kmp.designsystem.extensions.fixedScaleTextStyle
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_baseline_arrow_forward_ios_24
import mongsil.composeapp.generated.resources.year_month_format
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SimpleCalendarYearMonth(
    modifier: Modifier = Modifier,
    year: Int,
    month: Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(Res.string.year_month_format, year, month),
            textAlign = TextAlign.Center,
            style = fixedScaleTextStyle(MongsilTheme.typography.title3),
            color = MongsilTheme.colorScheme.labelStrong
        )
        HorizontalSpacer(8.dp)
        Icon(
            modifier = Modifier
                .size(20.dp)
                .circularRippleClickable(
                    radius = 18.dp
                ) {
                    onClick.invoke()
                },
            painter = painterResource(Res.drawable.ic_baseline_arrow_forward_ios_24),
            contentDescription = "forward",
            tint = MongsilTheme.colorScheme.labelStrong
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleCalendarYearMonthPreview() {
    MongsilTheme {
        SimpleCalendarYearMonth(
            year = 2026,
            month = 2,
        )
    }
}