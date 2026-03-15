package com.cashproject.mongsil.kmp.screen.diarychart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.Blue200
import com.cashproject.mongsil.kmp.designsystem.Gray300
import com.cashproject.mongsil.kmp.designsystem.Green200
import com.cashproject.mongsil.kmp.designsystem.Mint200
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.Orange200
import com.cashproject.mongsil.kmp.designsystem.Pink200
import com.cashproject.mongsil.kmp.designsystem.Purple200
import com.cashproject.mongsil.kmp.designsystem.RedOrange200
import com.cashproject.mongsil.kmp.designsystem.SkyBlue200
import com.cashproject.mongsil.kmp.designsystem.component.EmoticonImage
import com.cashproject.mongsil.kmp.designsystem.component.MongsilTopBar
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import com.cashproject.mongsil.kmp.model.ImageResource
import com.cashproject.mongsil.kmp.screen.diarychart.model.DiaryChartItem
import com.cashproject.mongsil.kmp.screen.diarychart.model.DiaryChartUiState
import com.cashproject.mongsil.kmp.screen.diarychart.model.WordCloudItem
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.chart_empty_emoticon
import mongsil.composeapp.generated.resources.chart_section_emoticon_stats
import mongsil.composeapp.generated.resources.chart_section_emoticon_top
import mongsil.composeapp.generated.resources.chart_section_word_cloud
import mongsil.composeapp.generated.resources.chart_streak_banner
import mongsil.composeapp.generated.resources.chart_word_cloud_empty
import mongsil.composeapp.generated.resources.emoticon_01
import mongsil.composeapp.generated.resources.ic_arrow_left_contained
import mongsil.composeapp.generated.resources.ic_arrow_right_contained
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiaryChartScreen(
    viewModel: DiaryChartViewModel,
    padding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DiaryChartScreenContent(
        uiState = uiState,
        onClose = onClose,
        onMovePrev = viewModel::moveToPreviousMonth,
        onMoveNext = viewModel::moveToNextMonth,
        modifier = Modifier.padding(padding),
    )
}

@Composable
private fun DiaryChartScreenContent(
    uiState: DiaryChartUiState,
    onClose: () -> Unit,
    onMovePrev: () -> Unit,
    onMoveNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
    ) {
        MongsilTopBar(
            leftContent = {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .circularRippleClickable(onClick = onClose),
                    painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
                    contentDescription = "닫기",
                    tint = MongsilTheme.colorScheme.labelStrong,
                )
            }
        )
        DiaryReportContent(
            uiState = uiState,
            onMovePrev = onMovePrev,
            onMoveNext = onMoveNext,
        )
    }
}

@Composable
private fun DiaryReportContent(
    uiState: DiaryChartUiState,
    onMovePrev: () -> Unit,
    onMoveNext: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 40.dp),
    ) {
        if (uiState.currentStreak >= 2) {
            item {
                StreakBanner(
                    streak = uiState.currentStreak,
                    modifier = Modifier.padding(horizontal = 20.dp)
                        .padding(top = 8.dp, bottom = 32.dp),
                )
            }
        }
        item {
            MonthHeader(
                year = uiState.year,
                month = uiState.month,
                canMoveNext = uiState.canMoveNextMonth,
                onMovePrev = onMovePrev,
                onMoveNext = onMoveNext,
            )
        }
        emoticonStatSection(items = uiState.items)
        wordCloudSection(items = uiState.wordCloudItems)
    }
}

@Composable
private fun StreakBanner(
    streak: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MongsilTheme.colorScheme.fill50,
                shape = RoundedCornerShape(16.dp),
            ).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.chart_streak_banner, streak),
            style = MongsilTheme.typography.heading2,
            color = MongsilTheme.colorScheme.labelStrong,
        )
    }
}

// 이모티콘 감정 통계 섹션
private fun LazyListScope.emoticonStatSection(items: List<DiaryChartItem>) {
    item {
        StatSectionHeader(
            title = stringResource(Res.string.chart_section_emoticon_top),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 16.dp),
        )
    }
    if (items.isEmpty()) {
        item { EmptyMessage() }
        return
    }
    item { TopEmoticons(items = items.take(3)) }
    item {
        StatSectionHeader(
            title = stringResource(Res.string.chart_section_emoticon_stats),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 0.dp),
        )
    }
    item { EmoticonCountList(items = items) }
}

@Composable
private fun StatSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = MongsilTheme.typography.headline1,
        color = MongsilTheme.colorScheme.labelStrong,
    )
}

@Composable
private fun MonthHeader(
    year: Int,
    month: Int,
    canMoveNext: Boolean,
    onMovePrev: () -> Unit,
    onMoveNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconArrow(enabled = true, isBack = true, onClick = onMovePrev)

        Column(
            modifier = Modifier.width(180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = year.toString(),
                style = MongsilTheme.typography.heading1,
                color = MongsilTheme.colorScheme.labelWeak
            )
            Text(
                text = "${month}월",
                style = MongsilTheme.typography.heading1,
                color = MongsilTheme.colorScheme.labelStrong
            )
        }

        IconArrow(enabled = canMoveNext, isBack = false, onClick = onMoveNext)
    }
}

@Composable
private fun IconArrow(
    enabled: Boolean,
    isBack: Boolean,
    onClick: () -> Unit,
) {
    val painter = if (isBack) {
        painterResource(Res.drawable.ic_arrow_left_contained)
    } else {
        painterResource(Res.drawable.ic_arrow_right_contained)
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .circularRippleClickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (enabled || isBack) {
            Icon(
                painter = painter,
                contentDescription = if (isBack) "이전 달" else "다음 달",
                tint = MongsilTheme.colorScheme.labelStrong
            )
        }
    }
}

@Composable
private fun TopEmoticons(items: List<DiaryChartItem>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items.forEach { item ->
            TopEmoticonItem(item = item)
        }
    }
}

@Composable
private fun TopEmoticonItem(item: DiaryChartItem) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmoticonImage(
            image = item.image,
            contentDescription = item.title,
            modifier = Modifier.size(70.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.title,
            style = MongsilTheme.typography.caption1,
            color = MongsilTheme.colorScheme.labelRegular,
        )
    }
}

@Composable
private fun EmoticonCountList(items: List<DiaryChartItem>) {
    val maxCount = items.maxOfOrNull { it.count } ?: 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            .background(
                color = MongsilTheme.colorScheme.fill50,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        items.forEach { item ->
            EmoticonCountRow(item = item, maxCount = maxCount)
        }
    }
}

@Composable
private fun EmoticonCountRow(
    item: DiaryChartItem,
    maxCount: Int,
) {
    val progress = if (maxCount == 0) 0f else item.count.toFloat() / maxCount.toFloat()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(64.dp),
        ) {
            EmoticonImage(
                image = item.image,
                contentDescription = item.title,
                modifier = Modifier.size(48.dp),
            )
            Text(
                text = item.title,
                style = MongsilTheme.typography.caption2,
                color = MongsilTheme.colorScheme.labelRegular,
                maxLines = 1,
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .background(Gray300, RoundedCornerShape(999.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(20.dp)
                    .background(parseHexColor(item.barColorHex), RoundedCornerShape(999.dp))
            )
        }

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = item.count.toString(),
            style = MongsilTheme.typography.heading1,
            color = MongsilTheme.colorScheme.labelStrong
        )
    }
}

@Composable
private fun EmptyMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.chart_empty_emoticon),
            style = MongsilTheme.typography.headline1,
            color = MongsilTheme.colorScheme.labelDisable
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryChartScreenContentEmptyPreview() {
    MongsilTheme {
        DiaryChartScreenContent(
            uiState = DiaryChartUiState(year = 2025, month = 3, items = emptyList()),
            onClose = {},
            onMovePrev = {},
            onMoveNext = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryChartScreenContentWithDataPreview() {
    MongsilTheme {
        DiaryChartScreenContent(
            uiState = DiaryChartUiState(
                year = 2025,
                month = 3,
                canMoveNextMonth = false,
                items = listOf(
                    DiaryChartItem(
                        emoticonId = 1,
                        image = ImageResource.Local(Res.drawable.emoticon_01),
                        title = "행복",
                        count = 10,
                        barColorHex = "#FFB347"
                    ),
                    DiaryChartItem(
                        emoticonId = 2,
                        image = ImageResource.Local(Res.drawable.emoticon_01),
                        title = "슬픔",
                        count = 6,
                        barColorHex = "#87CEEB"
                    ),
                    DiaryChartItem(
                        emoticonId = 3,
                        image = ImageResource.Local(Res.drawable.emoticon_01),
                        title = "평온",
                        count = 3,
                        barColorHex = "#90EE90"
                    ),
                ),
            ),
            onClose = {},
            onMovePrev = {},
            onMoveNext = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryChartScreenContentWithStreakPreview() {
    MongsilTheme {
        DiaryChartScreenContent(
            uiState = DiaryChartUiState(
                year = 2025,
                month = 3,
                currentStreak = 7,
                items = emptyList(),
            ),
            onClose = {},
            onMovePrev = {},
            onMoveNext = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WordCloudPreview() {
    MongsilTheme {
        DiaryChartScreenContent(
            uiState = DiaryChartUiState(
                year = 2025,
                month = 3,
                items = emptyList(),
                wordCloudItems = listOf(
                    WordCloudItem("행복", 15),
                    WordCloudItem("오늘도", 12),
                    WordCloudItem("커피", 10),
                    WordCloudItem("산책", 9),
                    WordCloudItem("힘들다", 8),
                    WordCloudItem("날씨", 7),
                    WordCloudItem("친구", 6),
                    WordCloudItem("맛있었다", 5),
                    WordCloudItem("피곤", 5),
                    WordCloudItem("좋아", 4),
                    WordCloudItem("하늘", 3),
                    WordCloudItem("기분", 3),
                ),
            ),
            onClose = {},
            onMovePrev = {},
            onMoveNext = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StreakBannerPreview() {
    MongsilTheme {
        StreakBanner(
            streak = 5,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        )
    }
}

private fun LazyListScope.wordCloudSection(items: List<WordCloudItem>) {
    item {
        StatSectionHeader(
            title = stringResource(Res.string.chart_section_word_cloud),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 16.dp),
        )
    }
    if (items.isEmpty()) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.chart_word_cloud_empty),
                    style = MongsilTheme.typography.headline1,
                    color = MongsilTheme.colorScheme.labelDisable,
                )
            }
        }
        return
    }
    item { WordCloudContent(items = items) }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordCloudContent(items: List<WordCloudItem>) {
    val maxCount = items.maxOfOrNull { it.count } ?: 1
    val minCount = items.minOfOrNull { it.count } ?: 1
    val countRange = (maxCount - minCount).coerceAtLeast(1).toFloat()

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.forEach { item ->
            WordChip(item = item, minCount = minCount, countRange = countRange)
        }
    }
}

private val wordChipColors = listOf(
    Purple200, Pink200, Blue200, Mint200, Orange200, Green200, RedOrange200, SkyBlue200
)

@Composable
private fun WordChip(
    item: WordCloudItem,
    minCount: Int,
    countRange: Float,
) {
    val ratio = (item.count - minCount) / countRange
    val fontSize = (14 + ratio * 18).sp
    val colorIndex = item.word.hashCode().mod(wordChipColors.size).let {
        if (it < 0) it + wordChipColors.size else it
    }
    val chipColor = wordChipColors[colorIndex]

    Box(
        modifier = Modifier
            .background(
                color = chipColor.copy(alpha = 0.4f + ratio * 0.5f),
                shape = RoundedCornerShape(999.dp),
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = item.word,
            fontSize = fontSize,
            color = MongsilTheme.colorScheme.labelStrong,
        )
    }
}

private fun parseHexColor(hex: String): Color {
    val value = hex.removePrefix("#")
    return try {
        val colorLong = when (value.length) {
            6 -> (0xFF000000L or value.toLong(16))
            8 -> value.toLong(16)
            else -> 0xFFD9D9D9L
        }
        Color(colorLong)
    } catch (_: IllegalArgumentException) {
        Gray300
    }
}