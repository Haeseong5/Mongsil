package com.cashproject.mongsil.kmp.screen.diarywrite.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme

/**
 * 일기 내용 입력 필드
 *
 * @param content 현재 일기 내용
 * @param onContentChange 내용 변경 콜백
 * @param enabled 입력 가능 여부
 */
@Composable
fun DiaryContentTextField(
    content: String,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    BasicTextField(
        value = content,
        onValueChange = onContentChange,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        enabled = enabled,
        textStyle = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.Black
        ),
        cursorBrush = SolidColor(Color.Black),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (content.isEmpty()) {
                    Text(
                        text = "오늘 하루는 어땠나요?",
                        style = MongsilTheme.typography.body1Normal.copy(
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        ),
                        color = Color.Gray.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        }
    )
}
