package com.cashproject.mongsil.ui.component

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
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.ui.theme.Gray400
import com.cashproject.mongsil.ui.theme.primaryTextStyle

@Composable
fun RoundedInputBox(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    maxLength: Int = Int.MAX_VALUE,
    onValueChange: (text: String) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                if (it.length <= maxLength) {
                    onValueChange.invoke(it)
                }
            },
            maxLines = 13,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ),
            textStyle = primaryTextStyle.copy(Color.White, fontSize = 22.sp),
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.BottomCenter)
                ) {
                    if (text.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            text = hint,
                            color = Gray400,
                            style = primaryTextStyle,
                            fontSize = 22.sp
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
            textStyle = primaryTextStyle,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxHeight()) {
                    if (input.isEmpty()) {
                        Text(
                            text = hint,
                            style = primaryTextStyle,
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
