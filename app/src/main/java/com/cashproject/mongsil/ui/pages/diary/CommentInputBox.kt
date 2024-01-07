package com.cashproject.mongsil.ui.pages.diary


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.data.db.entity.toEmoticon
import com.cashproject.mongsil.extension.log
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.component.RoundedInputBox
import com.cashproject.mongsil.ui.theme.dpToSp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommentInputBox(
    modifier: Modifier = Modifier,
    text: String,
    emoticonId: Int,
    onValueChange: (String) -> Unit = {},
    onClickEmoticon: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val imeVisible = WindowInsets.isImeVisible
    imeVisible.toString().log()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(
                if (imeVisible) Modifier.background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                ) else Modifier
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.noRippleClickable {
                onClickEmoticon.invoke()
            },
            painter = painterResource(emoticonId.toEmoticon().icon),
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
