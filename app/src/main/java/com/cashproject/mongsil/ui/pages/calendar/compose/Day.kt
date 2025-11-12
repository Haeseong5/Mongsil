package com.cashproject.mongsil.ui.pages.calendar.compose

import android.util.Log
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cashproject.mongsil.R
import com.cashproject.mongsil.ui.theme.dpToSp
import com.cashproject.mongsil.ui.theme.primaryTextColor
import com.cashproject.mongsil.ui.theme.primaryTextStyle
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.DayOfWeek

@Composable
fun BoxScope.Day(
    day: CalendarDay,
    isRecord: Boolean = false,
    emoticonImageUrl: String,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .size(36.dp)
            .clip(shape = CircleShape)
            .aspectRatio(1f)
            .align(Alignment.Center)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                when (day.position) {
                    DayPosition.MonthDate -> onClick.invoke()
                    else -> {}
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.alpha(
                when (day.position) {
                    DayPosition.MonthDate -> 1.0f
                    else -> 0.4f
                }
            ),
            text = if (isRecord) "" else day.date.dayOfMonth.toString(),
            fontWeight = if (isRecord || isToday(day)) FontWeight(700) else FontWeight(400),
            style = primaryTextStyle,
            fontSize = dpToSp(dp = 14.dp),
            color = when (day.date.dayOfWeek) {
                DayOfWeek.SUNDAY -> Color(context.getColor(R.color.sunday))
                DayOfWeek.SATURDAY -> Color(context.getColor(R.color.saturday))
                else -> primaryTextColor
            }
        )

        if (emoticonImageUrl.isNotEmpty()) {
            Log.d("++##", "emoticonImageUrl : $emoticonImageUrl")
            AsyncImage(
                modifier = Modifier.alpha(
                    when (day.position) {
                        DayPosition.MonthDate -> 1.0f
                        else -> 0.4f
                    }
                ),
                model = emoticonImageUrl,
                contentDescription = ""
            )
        }
    }
}
