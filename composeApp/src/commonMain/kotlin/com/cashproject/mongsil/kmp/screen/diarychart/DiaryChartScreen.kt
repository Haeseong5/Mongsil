package com.cashproject.mongsil.kmp.screen.diarychart

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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.cashproject.mongsil.kmp.designsystem.Gray300
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.screen.diarychart.model.DiaryChartItem
import com.cashproject.mongsil.kmp.screen.diarychart.model.DiaryChartUiState
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.chart_empty_emoticon
import mongsil.composeapp.generated.resources.chart_section_emoticon_stats
import mongsil.composeapp.generated.resources.chart_section_emoticon_top
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.ic_baseline_arrow_forward_ios_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiaryChartScreen(
    viewModel: DiaryChartViewModel,
    padding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .padding(padding)
    ) {
        CloseBar(onClose = onClose)
        DiaryReportContent(
            uiState = uiState,
            onMovePrev = viewModel::moveToPreviousMonth,
            onMoveNext = viewModel::moveToNextMonth,
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
        style = MongsilTheme.typography.heading1,
        color = MongsilTheme.colorScheme.labelStrong,
    )
}

@Composable
private fun CloseBar(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            modifier = Modifier.clickable { onClose() },
            text = "X",
            style = MongsilTheme.typography.heading1,
            color = MongsilTheme.colorScheme.labelStrong
        )
    }
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
            .fillMaxWidth()
            .padding(top = 8.dp),
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
                style = MongsilTheme.typography.title3,
                color = MongsilTheme.colorScheme.labelWeak
            )
            Text(
                text = "${month}월",
                style = MongsilTheme.typography.title2,
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
        painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24)
    } else {
        painterResource(Res.drawable.ic_baseline_arrow_forward_ios_24)
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (enabled || isBack) {
            androidx.compose.material3.Icon(
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
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "이모티콘",
                modifier = Modifier
                    .size(92.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun EmoticonCountList(items: List<DiaryChartItem>) {
    val maxCount = items.maxOfOrNull { it.count } ?: 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(
                color = MongsilTheme.colorScheme.fill50,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
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
        AsyncImage(
            model = item.imageUrl,
            contentDescription = "이모티콘",
            modifier = Modifier.size(64.dp)
        )

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
            style = MongsilTheme.typography.heading2,
            color = MongsilTheme.colorScheme.labelDisable
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