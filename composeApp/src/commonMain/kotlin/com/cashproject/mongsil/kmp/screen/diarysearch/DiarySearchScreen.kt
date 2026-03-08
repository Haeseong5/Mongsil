package com.cashproject.mongsil.kmp.screen.diarysearch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import com.cashproject.mongsil.kmp.screen.diarysearch.model.DiarySearchItem
import com.cashproject.mongsil.kmp.screen.diarysearch.model.DiarySearchUiState
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiarySearchScreen(
    padding: PaddingValues = PaddingValues(),
    onBack: () -> Unit = {},
    onDiaryClick: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> },
    viewModel: DiarySearchViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DiarySearchScreenContent(
        uiState = uiState,
        onBack = onBack,
        onQueryChange = viewModel::onQueryChange,
        onDiaryClick = onDiaryClick,
        modifier = Modifier.padding(padding)
    )
}

@Composable
private fun DiarySearchScreenContent(
    uiState: DiarySearchUiState,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onDiaryClick: (year: Int, month: Int, day: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
    ) {
        SearchTopBar(
            query = uiState.query,
            onQueryChange = onQueryChange,
            onBack = onBack,
            onClear = { onQueryChange("") }
        )

        when {
            uiState.query.isBlank() -> {
                SearchGuideMessage("검색어를 입력해 주세요")
            }

            uiState.results.isEmpty() -> {
                SearchGuideMessage("검색 결과가 없어요")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.results,
                        key = { it.id }
                    ) { diary ->
                        DiaryCard(
                            emoticonUrl = diary.emoticonImageUrl,
                            content = diary.content,
                            date = diary.date,
                            onClick = {
                                onDiaryClick(diary.year, diary.month, diary.day)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .circularRippleClickable(onClick = onBack),
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "뒤로 가기",
            tint = MongsilTheme.colorScheme.labelStrong
        )

        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            placeholder = {
                Text(
                    text = "일기 검색",
                    style = MongsilTheme.typography.body1Normal,
                    color = MongsilTheme.colorScheme.labelWeak
                )
            },
            shape = RoundedCornerShape(14.dp),
            textStyle = MongsilTheme.typography.body1Normal,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MongsilTheme.colorScheme.line,
                unfocusedBorderColor = MongsilTheme.colorScheme.line,
                cursorColor = MongsilTheme.colorScheme.labelStrong
            )
        )

        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color(0xFFC4C4C4))
                .clickable(enabled = query.isNotEmpty()) { onClear() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "x",
                style = MongsilTheme.typography.body1Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun SearchGuideMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MongsilTheme.typography.body1Normal,
            color = MongsilTheme.colorScheme.labelWeak
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiarySearchScreenContentEmptyQueryPreview() {
    MongsilTheme {
        DiarySearchScreenContent(
            uiState = DiarySearchUiState(query = "", results = emptyList()),
            onBack = {},
            onQueryChange = {},
            onDiaryClick = { _, _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiarySearchScreenContentNoResultPreview() {
    MongsilTheme {
        DiarySearchScreenContent(
            uiState = DiarySearchUiState(query = "행복", results = emptyList()),
            onBack = {},
            onQueryChange = {},
            onDiaryClick = { _, _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiarySearchScreenContentWithResultsPreview() {
    MongsilTheme {
        DiarySearchScreenContent(
            uiState = DiarySearchUiState(
                query = "행복",
                results = listOf(
                    DiarySearchItem(
                        id = 1L,
                        year = 2025,
                        month = 3,
                        day = 9,
                        content = "오늘은 정말 행복한 하루였다. 친구들과 맛있는 밥을 먹고 즐거운 시간을 보냈다.",
                        emoticonImageUrl = ""
                    ),
                    DiarySearchItem(
                        id = 2L,
                        year = 2025,
                        month = 2,
                        day = 14,
                        content = "행복한 발렌타인데이. 소소한 일상이 행복임을 느꼈다.",
                        emoticonImageUrl = ""
                    ),
                )
            ),
            onBack = {},
            onQueryChange = {},
            onDiaryClick = { _, _, _ -> }
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

    return "${year}.${month.toString().padStart(2, '0')}.${
        day.toString().padStart(2, '0')
    } $dayOfWeek"
}
