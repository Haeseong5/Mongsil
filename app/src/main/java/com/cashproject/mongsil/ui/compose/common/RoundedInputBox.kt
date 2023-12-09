package com.cashproject.mongsil.ui.compose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.ui.compose.extensions.VerticalSpacer
import com.cashproject.mongsil.ui.theme.Gray400
import com.cashproject.mongsil.ui.theme.latoTextStyle

@Composable
fun RoundedInputBox(
    modifier: Modifier = Modifier,
    maxLength: Int = Int.MAX_VALUE,
    hint: String = "오늘의 기분을 입력해주세요.",
    inputText: (String) -> Unit = { },
) {
    var input by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Color.Transparent),
    ) {
        BasicTextField(
            value = input,
            onValueChange = {
                if (it.length <= maxLength) input = it
                inputText.invoke(it)
            },
            maxLines = maxLength,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal =8.dp, vertical = 12.dp)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = CircleShape
                ),

            textStyle = latoTextStyle.copy(Color.White),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.BottomCenter)
                ) {
                    if (input.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            text = hint,
                            color = Gray400,
                            style = latoTextStyle,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        innerTextField()
                    }

                }
            }
        )
    }
}


@Composable
fun RoundedWideInputBox(
    modifier: Modifier = Modifier,
    maxLength: Int = Int.MAX_VALUE,
    hint: String = "운동 중에 느꼈던 점을 자유롭게 적어보세요",
    inputText: (String) -> Unit = { },
) {
    var input by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Color.White),
    ) {
        BasicTextField(
            value = input,
            onValueChange = {
                if (it.length <= maxLength) {
                    input = it
                    inputText.invoke(it)
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            textStyle = latoTextStyle,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxHeight()) {
                    if (input.isEmpty()) {
                        Text(
                            text = hint,
                            style = latoTextStyle,
//                            style = body2.copy(color = Gray400),
                        )
                    }
                    Box(
                        modifier = Modifier.padding(end = 20.dp)
                    ) {
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewRoundedInputBox() {
    Column(
        modifier = Modifier
            .background(color = Color.LightGray)
            .fillMaxSize()
    ) {
        VerticalSpacer(dp = 40.dp)
        RoundedInputBox(modifier = Modifier.padding(horizontal = 16.dp))
        VerticalSpacer(dp = 40.dp)
        RoundedWideInputBox(modifier = Modifier.padding(horizontal = 16.dp))
    }
}
