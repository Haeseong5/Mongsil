package com.cashproject.mongsil.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VerticalSpacer(
    modifier: Modifier = Modifier,
    dp: Dp
) {
    Spacer(modifier = modifier.height(dp))
}

@Composable
fun HorizontalSpacer(
    modifier: Modifier = Modifier,
    dp: Dp
) {
    Spacer(modifier = modifier.width(dp))
}