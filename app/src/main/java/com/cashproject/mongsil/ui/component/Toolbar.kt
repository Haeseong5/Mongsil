package com.cashproject.mongsil.ui.component

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.theme.gamjaflowerFamily

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

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackButtonClick: () -> Unit = {},
    onEndButtonClick: () -> Unit = {},
    visibleBack: Boolean = true,
    isVisibleSettingButton: Boolean = false,
) {
    Surface(
        modifier = Modifier
            .background(Color.White)
            .height(56.dp)
            .fillMaxWidth()
            .shadow(Color.Black.copy(alpha = 0.5f), offsetY = 1.dp, blurRadius = 1.dp)
            .padding(bottom = 4.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
        ) {
            if (visibleBack) {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterStart)
                        .size(24.dp)
                        .noRippleClickable {
                            onBackButtonClick.invoke()
                        },
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = ""
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                fontSize = 20.sp,
                fontFamily = gamjaflowerFamily
            )

            if (isVisibleSettingButton) {
                Icon(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
                        .noRippleClickable {
                            onEndButtonClick.invoke()
                        },
                    painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                    contentDescription = ""
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewToolbar() {
    Toolbar(title = "제목")
}