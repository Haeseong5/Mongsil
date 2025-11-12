package com.cashproject.mongsil.ui.pages.calendar.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NotificationBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(
                elevation = (1.5).dp,
                shape = CircleShape
            )
            .clip(shape = CircleShape)
            .size(9.dp)
            .background(color = Color.Red)
    ) { }
}
