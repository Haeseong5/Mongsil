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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryMonthlyItem
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiarySortOrder
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
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
        TopBar(
            year = uiState.year,
            month = uiState.month,
            sortOrder = uiState.sortOrder,
            canMoveNextMonth = uiState.canMoveNextMonth,
            onBack = onBack,
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
                        item = item,
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
    onBack: () -> Unit,
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
                .clickable { onBack() },
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "뒤로 가기",
            tint = MongsilTheme.colorScheme.labelStrong
        )

        Spacer(modifier = Modifier.width(10.dp))

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
private fun DiaryCard(
    item: DiaryMonthlyItem,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MongsilTheme.colorScheme.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (item.emoticonImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = item.emoticonImageUrl,
                    contentDescription = "감정 이모티콘",
                    modifier = Modifier.size(100.dp)
                )
            }

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = formatDate(item.year, item.month, item.day),
                style = MongsilTheme.typography.body1Normal,
                color = MongsilTheme.colorScheme.labelWeak
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                text = item.content,
                style = MongsilTheme.typography.body1Bold,
                color = MongsilTheme.colorScheme.labelStrong,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
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

private fun formatDate(year: Int, month: Int, day: Int): String {
    val date = LocalDate(year, month, day)
    val dayOfWeek = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> "월요일"
        DayOfWeek.TUESDAY -> "화요일"
        DayOfWeek.WEDNESDAY -> "수요일"
        DayOfWeek.THURSDAY -> "목요일"
        DayOfWeek.FRIDAY -> "금요일"
        DayOfWeek.SATURDAY -> "토요일"
        DayOfWeek.SUNDAY -> "일요일"
    }

    return "${year}.${month.toString().padStart(2, '0')}.${day.toString().padStart(2, '0')} $dayOfWeek"
}
