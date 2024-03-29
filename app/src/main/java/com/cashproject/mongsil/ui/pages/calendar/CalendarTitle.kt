package com.cashproject.mongsil.ui.pages.calendar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.theme.dpToSp
import com.cashproject.mongsil.ui.theme.primaryTextColor
import com.cashproject.mongsil.ui.theme.primaryTextStyle
import java.time.YearMonth

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier = Modifier,
    currentMonth: YearMonth,
    goToPrevious: () -> Unit = {},
    goToNext: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            icon = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "Previous",
            onClick = goToPrevious,
        )
        Text(
            modifier = Modifier.padding(horizontal = 30.dp),
            text = "${currentMonth.year}년 ${currentMonth.month.value}월",
            style = primaryTextStyle,
            fontSize = dpToSp(dp = 20.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = primaryTextColor
        )
        CalendarNavigationIcon(
            icon = painterResource(id = R.drawable.ic_baseline_arrow_forward_ios_24),
            contentDescription = "Next",
            onClick = goToNext,
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Icon(
        modifier = Modifier
            .size(20.dp)
            .noRippleClickable {
                onClick.invoke()
            },
        painter = icon,
        contentDescription = contentDescription,
        tint = primaryTextColor
    )
}

@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SimpleCalendarTitleDarkMode() {
    SimpleCalendarTitle(currentMonth = YearMonth.now())
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun SimpleCalendarTitleLightMode() {
    SimpleCalendarTitle(currentMonth = YearMonth.now())
}