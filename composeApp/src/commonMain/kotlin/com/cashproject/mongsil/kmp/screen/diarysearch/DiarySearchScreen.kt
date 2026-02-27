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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
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
                        DiarySearchCard(
                            item = diary,
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
            .height(56.dp)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable { onBack() },
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
private fun DiarySearchCard(
    item: DiarySearchItem,
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
                    modifier = Modifier.size(96.dp)
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
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
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
