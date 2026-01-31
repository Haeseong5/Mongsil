package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.Gray200
import com.cashproject.mongsil.kmp.designsystem.Gray300

@Composable
fun VerticalSpacer(height: Dp = 1.dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun HorizontalSpacer(width: Dp = 1.dp) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun HorizontalGraySpacer(
    modifier: Modifier = Modifier,
    height: Dp = 1.dp,
    color: Color = Gray200,
) {
    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .background(color)
    )
}

// 세로 회색바
@Composable
fun VerticalDivider(height: Dp = 16.dp, color: Color = Gray300) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(height)
            .background(color)
    )
}

// 점선
@Composable
fun DottedDivider(
    color: Color = Color.Gray,
    thickness: Dp = 1.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
) {
    val density = LocalDensity.current
    // Canvas의 높이는 thickness로, 너비는 부모에 꽉 채움
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        // dp → px 변환
        val dashPx = with(density) { dashLength.toPx() }
        val gapPx = with(density) { gapLength.toPx() }
        // 점선 효과
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f)
        // 실제 라인 그리기
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = thickness.toPx(),
            pathEffect = pathEffect
        )
    }
}