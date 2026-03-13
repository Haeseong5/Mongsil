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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.custom_color_circle_desc
import mongsil.composeapp.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
    modifier: Modifier = Modifier,
) {
    var showCustomPicker by remember { mutableStateOf(false) }

    if (showCustomPicker) {
        CustomColorPickerDialog(
            initialColor = selectedColor,
            onColorSelected = onColorSelected,
            onDismiss = { showCustomPicker = false },
        )
    }

    val isCustomColorSelected = selectedColor !in DIARY_TEXT_COLORS

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item {
            RainbowColorCircle(
                isSelected = isCustomColorSelected,
                selectedCustomColor = if (isCustomColorSelected) selectedColor else null,
                onClick = { showCustomPicker = true },
            )
        }
        items(DIARY_TEXT_COLORS) { color ->
            ColorCircle(
                color = color,
                isSelected = selectedColor == color,
                onColorSelected = onColorSelected,
            )
        }
    }
}

@Composable
private fun RainbowColorCircle(
    isSelected: Boolean,
    selectedCustomColor: Color?,
    onClick: () -> Unit,
) {
    val rainbowColors = listOf(
        Color(0xFFFF0000), Color(0xFFFFFF00), Color(0xFF00FF00),
        Color(0xFF00FFFF), Color(0xFF0000FF), Color(0xFFFF00FF), Color(0xFFFF0000),
    )
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .drawBehind {
                if (selectedCustomColor != null) {
                    drawCircle(color = selectedCustomColor)
                } else {
                    drawCircle(brush = Brush.sweepGradient(colors = rainbowColors))
                }
            }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color.White.copy(alpha = 0.9f)
                else Color.Black.copy(alpha = 0.1f),
                shape = CircleShape,
            )
            .circularRippleClickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected && selectedCustomColor != null) {
            val checkTint = selectedCustomColor.toHsvColor().let { hsv ->
                if (hsv.value > 0.5f) Color.Black.copy(alpha = 0.6f) else Color.White
            }
            Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = stringResource(Res.string.custom_color_circle_desc),
                tint = checkTint,
                modifier = Modifier.size(16.dp),
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
                color = if (isSelected) {
                    color.contrastColor().copy(alpha = 0.8f)
                } else {
                    Color.Black.copy(alpha = 0.1f)
                },
                shape = CircleShape,
            )
            .circularRippleClickable { onColorSelected(color) },
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = null,
                tint = color.contrastColor(),
                modifier = Modifier.size(16.dp),
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
            onColorSelected = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorPaletteCustomSelectedPreview() {
    MongsilTheme {
        ColorPalette(
            selectedColor = Color(0xFF2ECC71),
            onColorSelected = {},
        )
    }
}
