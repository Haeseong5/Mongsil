package com.cashproject.mongsil.kmp.screen.diarywrite.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.custom_color_picker_cancel
import mongsil.composeapp.generated.resources.custom_color_picker_confirm
import mongsil.composeapp.generated.resources.custom_color_selected_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialHsv = remember(initialColor) { initialColor.toHsvColor() }
    var hue by remember { mutableStateOf(initialHsv.hue) }
    var saturation by remember { mutableStateOf(initialHsv.saturation) }
    var value by remember { mutableStateOf(initialHsv.value) }

    val currentColor = hsvToColor(hue, saturation, value)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MongsilTheme.colorScheme.card),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SaturationValuePicker(
                    hue = hue,
                    saturation = saturation,
                    value = value,
                    onChanged = { s, v ->
                        saturation = s
                        value = v
                    },
                )
                HueSlider(
                    hue = hue,
                    onHueChanged = { hue = it },
                )
                ColorPreviewRow(color = currentColor)
                DialogButtons(
                    onDismiss = onDismiss,
                    onConfirm = {
                        onColorSelected(currentColor)
                        onDismiss()
                    },
                )
            }
        }
    }
}

@Composable
private fun SaturationValuePicker(
    hue: Float,
    saturation: Float,
    value: Float,
    onChanged: (saturation: Float, value: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var pickerSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .onSizeChanged { pickerSize = it }
            .pointerInput(pickerSize) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    applySaturationValue(down.position, pickerSize, onChanged)
                    drag(down.id) { change ->
                        change.consume()
                        applySaturationValue(change.position, pickerSize, onChanged)
                    }
                }
            },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val hueColor = hsvToColor(hue, 1f, 1f)
            drawRect(brush = Brush.horizontalGradient(listOf(Color.White, hueColor)))
            drawRect(brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))

            val cx = saturation * size.width
            val cy = (1f - value) * size.height
            drawCircle(
                color = Color.White,
                radius = 10.dp.toPx(),
                center = Offset(cx, cy),
                style = Stroke(width = 2.dp.toPx()),
            )
            drawCircle(
                color = Color.Black.copy(alpha = 0.3f),
                radius = 10.dp.toPx(),
                center = Offset(cx, cy),
                style = Stroke(width = 1.dp.toPx()),
            )
        }
    }
}

private fun applySaturationValue(
    position: Offset,
    size: IntSize,
    onChanged: (Float, Float) -> Unit,
) {
    if (size.width <= 0 || size.height <= 0) return
    val s = (position.x / size.width).coerceIn(0f, 1f)
    val v = 1f - (position.y / size.height).coerceIn(0f, 1f)
    onChanged(s, v)
}

@Composable
private fun HueSlider(
    hue: Float,
    onHueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var sliderSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp))
            .onSizeChanged { sliderSize = it }
            .pointerInput(sliderSize) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    applyHue(down.position, sliderSize, onHueChanged)
                    drag(down.id) { change ->
                        change.consume()
                        applyHue(change.position, sliderSize, onHueChanged)
                    }
                }
            },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFF0000), Color(0xFFFFFF00), Color(0xFF00FF00),
                        Color(0xFF00FFFF), Color(0xFF0000FF), Color(0xFFFF00FF), Color(0xFFFF0000),
                    ),
                ),
            )
            val cx = hue / 360f * size.width
            val radius = size.height / 2 - 2.dp.toPx()
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(cx, size.height / 2),
                style = Stroke(width = 2.dp.toPx()),
            )
        }
    }
}

private fun applyHue(position: Offset, size: IntSize, onHueChanged: (Float) -> Unit) {
    if (size.width <= 0) return
    val h = (position.x / size.width * 360f).coerceIn(0f, 360f)
    onHueChanged(h)
}

@Composable
private fun ColorPreviewRow(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape),
        )
        Text(
            text = stringResource(Res.string.custom_color_selected_label),
            style = MongsilTheme.typography.body1Medium,
            color = MongsilTheme.colorScheme.labelStrong,
        )
    }
}

@Composable
private fun DialogButtons(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(onClick = onDismiss) {
            Text(
                text = stringResource(Res.string.custom_color_picker_cancel),
                color = MongsilTheme.colorScheme.labelStrong,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(
                containerColor = MongsilTheme.colorScheme.labelStrong,
            ),
        ) {
            Text(
                text = stringResource(Res.string.custom_color_picker_confirm),
                color = MongsilTheme.colorScheme.background,
            )
        }
    }
}

internal data class HsvColor(
    val hue: Float,
    val saturation: Float,
    val value: Float,
)

internal fun Color.toHsvColor(): HsvColor {
    val max = maxOf(red, green, blue)
    val min = minOf(red, green, blue)
    val delta = max - min
    val v = max
    val s = if (max == 0f) 0f else delta / max
    val h = when {
        delta == 0f -> 0f
        max == red -> 60f * (((green - blue) / delta) % 6f)
        max == green -> 60f * (((blue - red) / delta) + 2f)
        else -> 60f * (((red - green) / delta) + 4f)
    }.let { if (it < 0f) it + 360f else it }
    return HsvColor(h, s, v)
}

internal fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
    if (saturation == 0f) return Color(value, value, value)
    val h = hue / 60f
    val i = h.toInt()
    val f = h - i
    val p = value * (1f - saturation)
    val q = value * (1f - saturation * f)
    val t = value * (1f - saturation * (1f - f))
    return when (i % 6) {
        0 -> Color(value, t, p)
        1 -> Color(q, value, p)
        2 -> Color(p, value, t)
        3 -> Color(p, q, value)
        4 -> Color(t, p, value)
        5 -> Color(value, p, q)
        else -> Color.Black
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomColorPickerDialogPreview() {
    MongsilTheme {
        CustomColorPickerDialog(
            initialColor = Color(0xFF1E88E5),
            onColorSelected = {},
            onDismiss = {},
        )
    }
}
