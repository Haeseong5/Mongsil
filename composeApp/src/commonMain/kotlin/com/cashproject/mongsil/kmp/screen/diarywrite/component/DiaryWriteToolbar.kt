package com.cashproject.mongsil.kmp.screen.diarywrite.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.IconToolbar
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import org.jetbrains.compose.resources.painterResource

/**
 * 일기 작성 화면의 상단 툴바
 *
 * @param year 선택된 년도
 * @param month 선택된 월
 * @param day 선택된 일
 * @param onBackClick 뒤로가기 버튼 클릭 콜백
 */
@Composable
fun DiaryWriteToolbar(
    year: Int,
    month: Int,
    day: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        IconToolbar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            leftContent = {
                Icon(
                    painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onBackClick),
                    tint = Color.Black
                )
            }
        )
        
        // 날짜 표시
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(vertical = 16.dp, horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "${year}년 ${month}월 ${day}일",
                style = MongsilTheme.typography.heading2.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Color.Black
            )
        }
    }
}
