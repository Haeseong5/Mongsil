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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.DiaryCard
import com.cashproject.mongsil.kmp.designsystem.component.ObserveErrorEffect
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryMonthlyItem
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryMonthlyUiState
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiarySortOrder
import com.cashproject.mongsil.kmp.screen.diarymonthly.model.DiaryViewMode
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.cd_navigate_back
import mongsil.composeapp.generated.resources.cd_next_month
import mongsil.composeapp.generated.resources.cd_previous_month
import mongsil.composeapp.generated.resources.diary_all_title
import mongsil.composeapp.generated.resources.diary_empty
import mongsil.composeapp.generated.resources.ic_arrow_left_contained
import mongsil.composeapp.generated.resources.ic_arrow_right_contained
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.load_more
import mongsil.composeapp.generated.resources.sort_latest
import mongsil.composeapp.generated.resources.sort_oldest
import mongsil.composeapp.generated.resources.view_mode_all
import mongsil.composeapp.generated.resources.view_mode_monthly
import mongsil.composeapp.generated.resources.year_month_format
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

    ObserveErrorEffect(viewModel.errorEvent)

    DiaryListScreenContent(
        uiState = uiState,
        onBack = onBack,
        onMovePrevMonth = viewModel::moveToPreviousMonth,
        onMoveNextMonth = viewModel::moveToNextMonth,
        onToggleSort = viewModel::toggleSortOrder,
        onSwitchViewMode = viewModel::switchViewMode,
        onDiaryClick = onDiaryClick,
        onLoadMore = viewModel::loadMoreDiaries,
        modifier = Modifier.padding(padding),
    )
}

@Composable
private fun DiaryListScreenContent(
    uiState: DiaryMonthlyUiState,
    onBack: () -> Unit,
    onMovePrevMonth: () -> Unit,
    onMoveNextMonth: () -> Unit,
    onToggleSort: () -> Unit,
    onSwitchViewMode: () -> Unit,
    onDiaryClick: (year: Int, month: Int, day: Int) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
    ) {
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp)
                .clickable { onBack() },
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = stringResource(Res.string.cd_navigate_back),
            tint = MongsilTheme.colorScheme.labelStrong,
        )

        TopBar(
            year = uiState.year,
            month = uiState.month,
            viewMode = uiState.viewMode,
            sortOrder = uiState.sortOrder,
            canMoveNextMonth = uiState.canMoveNextMonth,
            onMovePrevMonth = onMovePrevMonth,
            onMoveNextMonth = onMoveNextMonth,
            onToggleSort = onToggleSort,
            onSwitchViewMode = onSwitchViewMode,
        )

        when {
            uiState.isLoadingAll -> LoadingContent()
            uiState.displayedDiaries.isEmpty() -> EmptyMessage()
            else -> DiaryList(
                diaries = uiState.displayedDiaries,
                hasMorePages = uiState.hasMorePages,
                onDiaryClick = onDiaryClick,
                onLoadMore = onLoadMore,
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
            contentDescription = stringResource(Res.string.cd_previous_month),
            tint = MongsilTheme.colorScheme.labelStrong,
        )
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = stringResource(Res.string.year_month_format, year, month),
            style = MongsilTheme.typography.heading1,
            color = MongsilTheme.colorScheme.labelStrong,
        )
        Icon(
            modifier = Modifier
                .size(24.dp)
                .circularRippleClickable(enabled = canMoveNextMonth) { onMoveNextMonth() },
            painter = painterResource(Res.drawable.ic_arrow_right_contained),
            contentDescription = stringResource(Res.string.cd_next_month),
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
                emoticonImage = item.emoticonImage,
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
            text = stringResource(Res.string.diary_empty),
            style = MongsilTheme.typography.heading2,
            color = Color(0xFFBFBFBF),
        )
    }
}

private val previewDiaries = listOf(
    DiaryMonthlyItem(
        id = 1L,
        year = 2026,
        month = 3,
        day = 14,
        content = "오늘은 날씨가 맑고 기분이 좋았다. 오랜만에 공원을 산책하며 봄 기운을 느꼈다.",
        emoticonImage = null
    ),
    DiaryMonthlyItem(
        id = 2L,
        year = 2026,
        month = 3,
        day = 10,
        content = "바쁜 하루였지만 저녁에 따뜻한 차 한 잔으로 마무리했다.",
        emoticonImage = null
    ),
    DiaryMonthlyItem(
        id = 3L,
        year = 2026,
        month = 3,
        day = 5,
        content = "친구와 오랜만에 만나서 맛있는 밥을 먹었다. 행복한 시간이었다.",
        emoticonImage = null
    ),
)

@Preview(showBackground = true, name = "월별 - 일기 있음")
@Composable
private fun DiaryListMonthlyWithDiariesPreview() {
    MongsilTheme {
        DiaryListScreenContent(
            uiState = DiaryMonthlyUiState(
                year = 2026,
                month = 3,
                viewMode = DiaryViewMode.MONTHLY,
                sortOrder = DiarySortOrder.LATEST,
                monthlyDiaries = previewDiaries,
                canMoveNextMonth = false,
            ),
            onBack = {},
            onMovePrevMonth = {},
            onMoveNextMonth = {},
            onToggleSort = {},
            onSwitchViewMode = {},
            onDiaryClick = { _, _, _ -> },
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true, name = "월별 - 비어있음")
@Composable
private fun DiaryListMonthlyEmptyPreview() {
    MongsilTheme {
        DiaryListScreenContent(
            uiState = DiaryMonthlyUiState(
                year = 2026,
                month = 3,
                viewMode = DiaryViewMode.MONTHLY,
                monthlyDiaries = emptyList(),
                canMoveNextMonth = false,
            ),
            onBack = {},
            onMovePrevMonth = {},
            onMoveNextMonth = {},
            onToggleSort = {},
            onSwitchViewMode = {},
            onDiaryClick = { _, _, _ -> },
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true, name = "월별 - 다음달 이동 가능")
@Composable
private fun DiaryListMonthlyCanMoveNextPreview() {
    MongsilTheme {
        DiaryListScreenContent(
            uiState = DiaryMonthlyUiState(
                year = 2026,
                month = 2,
                viewMode = DiaryViewMode.MONTHLY,
                monthlyDiaries = previewDiaries,
                canMoveNextMonth = true,
            ),
            onBack = {},
            onMovePrevMonth = {},
            onMoveNextMonth = {},
            onToggleSort = {},
            onSwitchViewMode = {},
            onDiaryClick = { _, _, _ -> },
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true, name = "월별 - 오래된 순 정렬")
@Composable
private fun DiaryListMonthlyOldestSortPreview() {
    MongsilTheme {
        DiaryListScreenContent(
            uiState = DiaryMonthlyUiState(
                year = 2026,
                month = 3,
                viewMode = DiaryViewMode.MONTHLY,
                sortOrder = DiarySortOrder.OLDEST,
                monthlyDiaries = previewDiaries,
                canMoveNextMonth = false,
            ),
            onBack = {},
            onMovePrevMonth = {},
            onMoveNextMonth = {},
            onToggleSort = {},
            onSwitchViewMode = {},
            onDiaryClick = { _, _, _ -> },
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true, name = "전체 - 일기 있음")
@Composable
private fun DiaryListAllWithDiariesPreview() {
    MongsilTheme {
        DiaryListScreenContent(
            uiState = DiaryMonthlyUiState(
                year = 2026,
                month = 3,
                viewMode = DiaryViewMode.ALL,
                sortOrder = DiarySortOrder.LATEST,
                allDiaries = previewDiaries,
                allDiariesTotalCount = 3,
            ),
            onBack = {},
            onMovePrevMonth = {},
            onMoveNextMonth = {},
            onToggleSort = {},
            onSwitchViewMode = {},
            onDiaryClick = { _, _, _ -> },
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true, name = "전체 - 더 불러오기 버튼")
@Composable
private fun DiaryListAllWithLoadMorePreview() {
    MongsilTheme {
        DiaryListScreenContent(
            uiState = DiaryMonthlyUiState(
                year = 2026,
                month = 3,
                viewMode = DiaryViewMode.ALL,
                sortOrder = DiarySortOrder.LATEST,
                allDiaries = previewDiaries,
                allDiariesTotalCount = 30,
            ),
            onBack = {},
            onMovePrevMonth = {},
            onMoveNextMonth = {},
            onToggleSort = {},
            onSwitchViewMode = {},
            onDiaryClick = { _, _, _ -> },
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true, name = "전체 - 비어있음")
@Composable
private fun DiaryListAllEmptyPreview() {
    MongsilTheme {
        DiaryListScreenContent(
            uiState = DiaryMonthlyUiState(
                year = 2026,
                month = 3,
                viewMode = DiaryViewMode.ALL,
                allDiaries = emptyList(),
                allDiariesTotalCount = 0,
            ),
            onBack = {},
            onMovePrevMonth = {},
            onMoveNextMonth = {},
            onToggleSort = {},
            onSwitchViewMode = {},
            onDiaryClick = { _, _, _ -> },
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true, name = "로딩 중")
@Composable
private fun DiaryListLoadingPreview() {
    MongsilTheme {
        DiaryListScreenContent(
            uiState = DiaryMonthlyUiState(
                year = 2026,
                month = 3,
                viewMode = DiaryViewMode.ALL,
                isLoadingAll = true,
            ),
            onBack = {},
            onMovePrevMonth = {},
            onMoveNextMonth = {},
            onToggleSort = {},
            onSwitchViewMode = {},
            onDiaryClick = { _, _, _ -> },
            onLoadMore = {},
        )
    }
}
