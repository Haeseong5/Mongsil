package com.cashproject.mongsil.kmp.screen.setting.fontstyle.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.kmp.designsystem.Gray300
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.screen.setting.fontstyle.FontStyleViewModel

@Composable
fun TextSizeControl(
    fontScale: Float,
    onScaleChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val minScale = FontStyleViewModel.MIN_FONT_SCALE
    val maxScale = FontStyleViewModel.MAX_FONT_SCALE

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "A",
            style = MongsilTheme.typography.body1Medium.copy(fontSize = 24.sp),
            color = MongsilTheme.colorScheme.labelRegular
        )

        Column(modifier = Modifier.weight(1f)) {
            Slider(
                value = fontScale,
                onValueChange = onScaleChange,
                valueRange = minScale..maxScale,
                steps = 5,
                colors = SliderDefaults.colors(
                    thumbColor = MongsilTheme.colorScheme.labelStrong,
                    activeTrackColor = Gray300,
                    inactiveTrackColor = Gray300,
                    activeTickColor = Gray300,
                    inactiveTickColor = Gray300
                )
            )
        }

        Text(
            text = "A",
            style = MongsilTheme.typography.body1Medium.copy(fontSize = 36.sp),
            color = MongsilTheme.colorScheme.labelRegular
        )
    }
}

@Preview
@Composable
private fun TextSizeControlPreview() {
    MongsilTheme {
        TextSizeControl(
            fontScale = 1.0f,
            onScaleChange = {}
        )
    }
}
