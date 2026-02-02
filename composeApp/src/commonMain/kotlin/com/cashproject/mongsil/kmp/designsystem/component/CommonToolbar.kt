package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CommonToolbar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {

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
            .height(44.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 왼쪽 아이콘 영역
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leftContent()
        }

        // 오른쪽 아이콘 영역
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            rightContent()
        }
    }
}
