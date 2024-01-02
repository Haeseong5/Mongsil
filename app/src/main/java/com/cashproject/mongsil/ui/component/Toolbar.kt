package com.cashproject.mongsil.ui.component

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.theme.regularFont

fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + topPixel
            val bottomPixel = size.height + leftPixel

            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
        }
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: String = "",
    navigationIcon: ImageVector? = null,
    navigationIconClicked: () -> Unit = {},
    actionIcon: ImageVector? = null,
    actionIconClicked: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                fontFamily = regularFont
            )
        },
        navigationIcon = {
            navigationIcon?.let {
                Image(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .noRippleClickable(onClick = navigationIconClicked),
                    imageVector = navigationIcon,
                    contentDescription = ""
                )
            }
        },
        actions = {
            actionIcon?.let {
                Image(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .noRippleClickable(onClick = actionIconClicked),
                    imageVector = it,
                    contentDescription = ""
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewToolbar() {
    Toolbar(title = "제목")
}