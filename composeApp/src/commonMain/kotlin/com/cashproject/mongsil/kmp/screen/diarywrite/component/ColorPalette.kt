package com.cashproject.mongsil.kmp.screen.diarywrite.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource

val DIARY_TEXT_COLORS = listOf(
    Color(0xFF000000),
    Color(0xFF616161),
    Color(0xFFBDBDBD),
    Color(0xFFE53935),
    Color(0xFFF4511E),
    Color(0xFFF9A825),
    Color(0xFF43A047),
    Color(0xFF1E88E5),
    Color(0xFF8E24AA),
    Color(0xFFD81B60),
)

@Composable
fun ColorPalette(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(DIARY_TEXT_COLORS) { color ->
            ColorCircle(
                color = color,
                isSelected = selectedColor == color,
                onColorSelected = onColorSelected
            )
        }
    }
}

@Composable
private fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onColorSelected: (Color) -> Unit,
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) color.contrastColor().copy(alpha = 0.8f)
                        else Color.Black.copy(alpha = 0.1f),
                shape = CircleShape
            )
            .circularRippleClickable { onColorSelected(color) },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = null,
                tint = color.contrastColor(),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private fun Color.contrastColor(): Color {
    val luminance = red * 0.299f + green * 0.587f + blue * 0.114f
    return if (luminance > 0.5f) Color.Black else Color.White
}

@Preview(showBackground = true)
@Composable
private fun ColorPalettePreview() {
    MongsilTheme {
        ColorPalette(
            selectedColor = Color(0xFF1E88E5),
            onColorSelected = {}
        )
    }
}
