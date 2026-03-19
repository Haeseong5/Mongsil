package com.cashproject.mongsil.kmp.screen.setting.fontstyle.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.kmp.designsystem.Gray300
import com.cashproject.mongsil.kmp.designsystem.LocalDarkTheme
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.extensions.fixedScaleTextStyle
import com.cashproject.mongsil.kmp.screen.setting.fontstyle.FontStyleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextSizeControl(
    fontScale: Float,
    onScaleChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val minScale = FontStyleViewModel.MIN_FONT_SCALE
    val maxScale = FontStyleViewModel.MAX_FONT_SCALE
    val isDark = LocalDarkTheme.current
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "A",
            style = fixedScaleTextStyle(MongsilTheme.typography.default),
            color = MongsilTheme.colorScheme.labelRegular
        )


        Slider(
            modifier = Modifier.weight(1f),
            value = fontScale,
            onValueChange = onScaleChange,
            valueRange = minScale..maxScale,
            steps = 5,
            thumb = {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(color = if(isDark) Color.Black else Color.White, shape = CircleShape)
                        .border(
                            width = 2.dp,
                            color = if(isDark) Color.White else Color.Black,
                            shape = CircleShape
                        )
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(2.dp),
                    thumbTrackGapSize = 4.dp,
                    drawTick = { offset, color ->
                        drawCircle(
                            color = color,
                            radius = 4.dp.toPx(),
                            center = offset
                        )
                    },
                    drawStopIndicator = { offset ->
                        drawCircle(
                            color = Gray300,
                            radius = 4.dp.toPx(),
                            center = offset
                        )
                    },
                    colors = SliderDefaults.colors(
                        activeTrackColor = Gray300,
                        inactiveTrackColor = Gray300,
                        activeTickColor = Gray300,
                        inactiveTickColor = Gray300
                    )
                )
            }
        )

        Text(
            text = "A",
            style = MongsilTheme.typography.default.copy(fontSize = 26.sp),
            color = MongsilTheme.colorScheme.labelRegular
        )
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextSizeControlPreview() {
    MongsilTheme {
        TextSizeControl(
            fontScale = 1.0f,
            onScaleChange = {}
        )
    }
}
