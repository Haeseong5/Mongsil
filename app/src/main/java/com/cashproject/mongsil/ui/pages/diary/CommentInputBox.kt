package com.cashproject.mongsil.ui.pages.diary


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.component.RoundedInputBox
import com.cashproject.mongsil.ui.theme.dpToSp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommentInputBox(
    modifier: Modifier = Modifier,
    text: String,
    emoticonUrl: String,
    onValueChange: (String) -> Unit = {},
    onClickEmoticon: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val imeVisible = WindowInsets.isImeVisible
    val topColor: Color by animateColorAsState(
        targetValue = if (imeVisible) Color.Black.copy(0.2f) else Color.Transparent,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing,
        ),
        label = ""
    )
    val bottomColor: Color by animateColorAsState(
        targetValue = if (imeVisible) Color.Black.copy(0.7f) else Color.Transparent,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing,
        ),
        label = ""
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        topColor,
                        bottomColor
                    )
                )
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(40.dp)
                .noRippleClickable {
                    onClickEmoticon.invoke()
                },
            model = emoticonUrl,
            contentDescription = "",
        )

        RoundedInputBox(
            modifier = Modifier.weight(1f),
            text = text,
            onValueChange = onValueChange,
            hint = "오늘의 기분을 입력해주세요.",
        )
        Text(
            modifier = Modifier.noRippleClickable {
                onConfirm.invoke()
            },
            text = "확인",
            color = Color.White,
            fontSize = dpToSp(dp = 18.dp),
            textAlign = TextAlign.Center
        )
    }
}
