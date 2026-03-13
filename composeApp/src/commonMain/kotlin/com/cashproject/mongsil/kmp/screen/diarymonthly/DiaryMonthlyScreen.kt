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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.DiaryCard
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiarySortOrder
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryViewMode
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.diary_all_title
import mongsil.composeapp.generated.resources.ic_arrow_left_contained
import mongsil.composeapp.generated.resources.ic_arrow_right_contained
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.load_more
import mongsil.composeapp.generated.resources.sort_latest
import mongsil.composeapp.generated.resources.sort_oldest
import mongsil.composeapp.generated.resources.view_mode_all
import mongsil.composeapp.generated.resources.view_mode_monthly
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiaryListScreen(
    viewModel: DiaryListViewModel,
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
                .padding(16.dp)
                .size(24.dp)
                .clickable { onBack() },
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "뒤로 가기",
            tint = MongsilTheme.colorScheme.labelStrong,
        )

        TopBar(
            year = uiState.year,
            month = uiState.month,
            viewMode = uiState.viewMode,
            sortOrder = uiState.sortOrder,
            canMoveNextMonth = uiState.canMoveNextMonth,
            onMovePrevMonth = viewModel::moveToPreviousMonth,
            onMoveNextMonth = viewModel::moveToNextMonth,
            onToggleSort = viewModel::toggleSortOrder,
            onSwitchViewMode = viewModel::switchViewMode,
        )

        when {
            uiState.isLoadingAll -> LoadingContent()
            uiState.displayedDiaries.isEmpty() -> EmptyMessage()
            else -> DiaryList(
                diaries = uiState.displayedDiaries,
                hasMorePages = uiState.hasMorePages,
                onDiaryClick = onDiaryClick,
                onLoadMore = viewModel::loadMoreDiaries,
            )
        }
    }
}

@Composable
private fun TopBar(
    year: Int,
    month: Int,
    viewMode: DiaryViewMode,
    sortOrder: DiarySortOrder,
    canMoveNextMonth: Boolean,
    onMovePrevMonth: () -> Unit,
    onMoveNextMonth: () -> Unit,
    onToggleSort: () -> Unit,
    onSwitchViewMode: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (viewMode) {
            DiaryViewMode.MONTHLY -> MonthlyTitle(
                year = year,
                month = month,
                canMoveNextMonth = canMoveNextMonth,
                onMovePrevMonth = onMovePrevMonth,
                onMoveNextMonth = onMoveNextMonth,
            )

            DiaryViewMode.ALL -> Text(
                text = stringResource(Res.string.diary_all_title),
                style = MongsilTheme.typography.heading1,
                color = MongsilTheme.colorScheme.labelStrong,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ViewModeToggle(viewMode = viewMode, onSwitchViewMode = onSwitchViewMode)

        SortButton(sortOrder = sortOrder, onToggleSort = onToggleSort)
    }
}

@Composable
private fun MonthlyTitle(
    year: Int,
    month: Int,
    canMoveNextMonth: Boolean,
    onMovePrevMonth: () -> Unit,
    onMoveNextMonth: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .circularRippleClickable { onMovePrevMonth() },
            painter = painterResource(Res.drawable.ic_arrow_left_contained),
            contentDescription = "이전 달",
            tint = MongsilTheme.colorScheme.labelStrong,
        )
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "${year}년 ${month}월",
            style = MongsilTheme.typography.heading1,
            color = MongsilTheme.colorScheme.labelStrong,
        )
        Icon(
            modifier = Modifier
                .size(24.dp)
                .circularRippleClickable(enabled = canMoveNextMonth) { onMoveNextMonth() },
            painter = painterResource(Res.drawable.ic_arrow_right_contained),
            contentDescription = "다음 달",
            tint = if (canMoveNextMonth) MongsilTheme.colorScheme.labelStrong
            else MongsilTheme.colorScheme.labelDisable,
        )
    }
}

@Composable
private fun ViewModeToggle(
    viewMode: DiaryViewMode,
    onSwitchViewMode: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MongsilTheme.colorScheme.labelDisable.copy(alpha = 0.15f))
            .clickable { onSwitchViewMode() }
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = when (viewMode) {
                DiaryViewMode.MONTHLY -> stringResource(Res.string.view_mode_all)
                DiaryViewMode.ALL -> stringResource(Res.string.view_mode_monthly)
            },
            style = MongsilTheme.typography.label1,
            color = MongsilTheme.colorScheme.labelStrong,
        )
    }
}

@Composable
private fun SortButton(
    sortOrder: DiarySortOrder,
    onToggleSort: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MongsilTheme.colorScheme.labelDisable.copy(alpha = 0.15f))
            .clickable { onToggleSort() }
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = if (sortOrder == DiarySortOrder.LATEST) {
                stringResource(Res.string.sort_latest)
            } else {
                stringResource(Res.string.sort_oldest)
            },
            style = MongsilTheme.typography.label1,
            color = MongsilTheme.colorScheme.labelStrong,
        )
    }
}

@Composable
private fun DiaryList(
    diaries: List<com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryMonthlyItem>,
    hasMorePages: Boolean,
    onDiaryClick: (year: Int, month: Int, day: Int) -> Unit,
    onLoadMore: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = diaries, key = { it.id }) { item ->
            DiaryCard(
                emoticonUrl = item.emoticonImageUrl,
                content = item.content,
                date = item.date,
                onClick = { onDiaryClick(item.year, item.month, item.day) },
            )
        }

        if (hasMorePages) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    TextButton(onClick = onLoadMore) {
                        Text(
                            text = stringResource(Res.string.load_more),
                            style = MongsilTheme.typography.body1Medium,
                            color = MongsilTheme.colorScheme.labelStrong,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MongsilTheme.colorScheme.labelWeak,
            modifier = Modifier.size(32.dp),
        )
    }
}

@Composable
private fun EmptyMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "작성된 일기가 없어요",
            style = MongsilTheme.typography.heading2,
            color = Color(0xFFBFBFBF),
        )
    }
}
