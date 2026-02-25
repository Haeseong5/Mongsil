package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import org.jetbrains.compose.resources.painterResource


@Composable
fun CommonToolbar(
    modifier: Modifier = Modifier,
    color: Color = MongsilTheme.colorScheme.background,
    onBack: () -> Unit = {},
    title: String = "",
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color)
            .padding(horizontal = 20.dp),
    ) {
        Icon(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterStart)
                .clickable { onBack() },
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "뒤로 가기",
            tint = MongsilTheme.colorScheme.labelStrong
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = MongsilTheme.typography.body1Medium,
            color = MongsilTheme.colorScheme.labelStrong
        )
    }
}

/**
 * 왼쪽과 오른쪽에 아이콘 버튼들을 배치할 수 있는 툴바
 *
 * @param modifier 툴바에 적용할 Modifier
 * @param leftContent 툴바 왼쪽에 배치할 아이콘들
 * @param rightContent 툴바 오른쪽에 배치할 아이콘들
 */
@Composable
fun IconToolbar(
    modifier: Modifier = Modifier,
    leftContent: @Composable RowScope.() -> Unit = {},
    rightContent: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 왼쪽 아이콘 영역
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leftContent()
        }

        // 오른쪽 아이콘 영역
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            rightContent()
        }
    }
}
