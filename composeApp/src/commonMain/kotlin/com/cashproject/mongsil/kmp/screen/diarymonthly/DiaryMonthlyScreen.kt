package com.cashproject.mongsil.kmp.screen.diarymonthly

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.DiaryCard
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiarySortOrder
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.ic_baseline_arrow_forward_ios_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun DiaryMonthlyScreen(
    viewModel: DiaryMonthlyViewModel,
    padding: PaddingValues = PaddingValues(),
    onBack: () -> Unit = {},
    onDiaryClick: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .padding(padding)
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
                .clickable { onBack() },
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "뒤로 가기",
            tint = MongsilTheme.colorScheme.labelStrong
        )

        TopBar(
            year = uiState.year,
            month = uiState.month,
            sortOrder = uiState.sortOrder,
            canMoveNextMonth = uiState.canMoveNextMonth,
            onMovePrevMonth = viewModel::moveToPreviousMonth,
            onMoveNextMonth = viewModel::moveToNextMonth,
            onToggleSort = viewModel::toggleSortOrder
        )

        if (uiState.diaries.isEmpty()) {
            EmptyMessage()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = uiState.diaries,
                    key = { it.id }
                ) { item ->
                    DiaryCard(
                        emoticonUrl = item.emoticonImageUrl,
                        content = item.content,
                        date = item.date,
                        onClick = { onDiaryClick(item.year, item.month, item.day) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    year: Int,
    month: Int,
    sortOrder: DiarySortOrder,
    canMoveNextMonth: Boolean,
    onMovePrevMonth: () -> Unit,
    onMoveNextMonth: () -> Unit,
    onToggleSort: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable { onMovePrevMonth() },
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "이전 달",
            tint = MongsilTheme.colorScheme.labelStrong
        )

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "${year}년 ${month}월",
            style = MongsilTheme.typography.heading1,
            color = MongsilTheme.colorScheme.labelStrong
        )

        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable(enabled = canMoveNextMonth) { onMoveNextMonth() },
            painter = painterResource(Res.drawable.ic_baseline_arrow_forward_ios_24),
            contentDescription = "다음 달",
            tint = if (canMoveNextMonth) {
                MongsilTheme.colorScheme.labelStrong
            } else {
                MongsilTheme.colorScheme.labelDisable
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.clickable { onToggleSort() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (sortOrder == DiarySortOrder.LATEST) "최신순" else "오래된 순",
                style = MongsilTheme.typography.heading2,
                color = MongsilTheme.colorScheme.labelStrong
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "v",
                style = MongsilTheme.typography.heading2,
                color = MongsilTheme.colorScheme.labelStrong
            )
        }
    }
}

@Composable
private fun EmptyMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "작성된 일기가 없어요",
            style = MongsilTheme.typography.heading2,
            color = Color(0xFFBFBFBF)
        )
    }
}