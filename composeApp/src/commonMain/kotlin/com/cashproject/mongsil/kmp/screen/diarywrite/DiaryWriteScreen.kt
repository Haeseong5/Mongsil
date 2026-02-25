package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.EmoticonBottomSheet
import com.cashproject.mongsil.kmp.designsystem.component.rememberSnackbarController
import com.cashproject.mongsil.kmp.model.Emoticon
import com.cashproject.mongsil.kmp.screen.diarywrite.component.BottomToolbar
import com.cashproject.mongsil.kmp.screen.diarywrite.component.ExitConfirmDialog
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteEvent
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteSideEffect
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteUiState
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.ic_menu
import mongsil.composeapp.generated.resources.ic_plus
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

/**
 * 일기 작성 화면
 *
 * @param viewModel 일기 작성 ViewModel
 * @param onSaveSuccess 저장 성공 콜백
 */
@Composable
fun DiaryWriteScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    onSaveSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: DiaryWriteViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarController = rememberSnackbarController()

    // SideEffect 처리
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is DiaryWriteSideEffect.SaveSuccess -> {
                    snackbarController.showSnackbar("일기가 저장되었습니다.")
                    onSaveSuccess()
                }

                DiaryWriteSideEffect.OnBack -> {
                    onBack.invoke()
                }
            }
        }
    }

    DiaryWriteScreenContent(
        modifier = modifier.padding(padding).consumeWindowInsets(padding),
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryWriteScreenContent(
    uiState: DiaryWriteUiState,
    onEvent: (DiaryWriteEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // 시스템 뒤로가기 처리 (BottomSheet나 Dialog가 없을 때만 활성화)
    BackPressHandler(enabled = !uiState.showEmoticonBottomSheet && !uiState.showExitDialog) {
        println("DiaryWriteScreen BackPressHandler called")
        onEvent(DiaryWriteEvent.OnBackPressed)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 상단 툴바
            TopBar(
                onBackClick = { onEvent(DiaryWriteEvent.OnBackClick) },
                onMoreClick = { /* TODO: 더보기 메뉴 */ }
            )

            // 메인 콘텐츠
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // 이모티콘 버튼
                EmoticonButton(
                    selectedEmoticon = uiState.selectedEmoticon,
                    onClick = { onEvent(DiaryWriteEvent.OnEmoticonButtonClick) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 날짜 표시
                DateText(
                    year = uiState.year,
                    month = uiState.month,
                    day = uiState.day
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 일기 내용 입력
                DiaryTextField(
                    content = uiState.content,
                    onContentChange = { onEvent(DiaryWriteEvent.OnContentChange(it)) },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            // 하단 툴바
            BottomToolbar(
                onSaveClick = { onEvent(DiaryWriteEvent.OnSaveClick) },
                canSave = uiState.hasContent,
                isLoading = uiState.isLoading
            )
        }

        // 이모티콘 바텀시트
        if (uiState.showEmoticonBottomSheet) {
            EmoticonBottomSheet(
                emoticons = uiState.emoticons,
                onDismiss = { onEvent(DiaryWriteEvent.OnEmoticonBottomSheetDismiss) },
                onEmoticonSelected = { onEvent(DiaryWriteEvent.OnEmoticonSelected(it)) }
            )
        }

        // 종료 확인 다이얼로그
        if (uiState.showExitDialog) {
            ExitConfirmDialog(
                onConfirm = { onEvent(DiaryWriteEvent.OnExitConfirm) },
                onDismiss = { onEvent(DiaryWriteEvent.OnExitCancel) }
            )
        }
    }
}

@Composable
private fun TopBar(
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
                contentDescription = "back button",
                modifier = Modifier.size(24.dp)
            )
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onMoreClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_menu), // TODO 아이콘 변경
                contentDescription = "back button",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun EmoticonButton(
    selectedEmoticon: Emoticon?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color(0xFFF5F5F5))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selectedEmoticon != null) {
            AsyncImage(
                model = selectedEmoticon.imageUrl,
                contentDescription = selectedEmoticon.title,
                modifier = Modifier.size(40.dp)
            )
        } else {
            Icon(
                painter = painterResource(Res.drawable.ic_plus),
                contentDescription = "back button",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun DateText(
    year: Int,
    month: Int,
    day: Int,
    modifier: Modifier = Modifier
) {
    val dayOfWeek = getDayOfWeekText(year, month, day)
    val dateText =
        "${year}.${month.toString().padStart(2, '0')}.${day.toString().padStart(2, '0')} $dayOfWeek"

    Text(
        text = dateText,
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Gray
        ),
        modifier = modifier
    )
}

@Composable
private fun DiaryTextField(
    content: String,
    onContentChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = content,
        onValueChange = onContentChange,
        modifier = modifier
            .padding(horizontal = 20.dp),
        enabled = enabled,
        textStyle = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.Black
        ),
        cursorBrush = SolidColor(Color.Black),
        decorationBox = { innerTextField ->
            Box {
                if (content.isEmpty()) {
                    Text(
                        text = "오늘 하루를 기록해보세요",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                }
                innerTextField()
            }
        }
    )
}


// 요일 텍스트 반환
private fun getDayOfWeekText(year: Int, month: Int, day: Int): String {
    // 간단한 요일 계산 (Zeller's congruence 알고리즘)
    val adjustedMonth = if (month < 3) month + 12 else month
    val adjustedYear = if (month < 3) year - 1 else year
    val k = adjustedYear % 100
    val j = adjustedYear / 100
    val h = (day + (13 * (adjustedMonth + 1) / 5) + k + k / 4 + j / 4 - 2 * j) % 7

    return when ((h + 6) % 7) {
        0 -> "월요일"
        1 -> "화요일"
        2 -> "수요일"
        3 -> "목요일"
        4 -> "금요일"
        5 -> "토요일"
        6 -> "일요일"
        else -> ""
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteScreenContentPreview() {
    MongsilTheme {
        DiaryWriteScreenContent(
            uiState = DiaryWriteUiState(
                year = 2026,
                month = 2,
                day = 4,
                content = "오늘은 날씨가 정말 좋았다.\n아침부터 햇살이 따스하게 비춰서 기분이 좋았다.",
                selectedEmoticon = null,
                emoticons = emptyList(),
                showEmoticonBottomSheet = false,
                isLoading = false,
                showExitDialog = false
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteScreenContentEmptyPreview() {
    MongsilTheme {
        DiaryWriteScreenContent(
            uiState = DiaryWriteUiState(
                year = 2026,
                month = 2,
                day = 4,
                content = "",
                selectedEmoticon = null,
                emoticons = emptyList(),
                showEmoticonBottomSheet = false,
                isLoading = false,
                showExitDialog = false
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteScreenContentWithEmoticonPreview() {
    MongsilTheme {
        DiaryWriteScreenContent(
            uiState = DiaryWriteUiState(
                year = 2026,
                month = 2,
                day = 4,
                content = "",
                selectedEmoticon = Emoticon(
                    id = 1,
                    title = "행복",
                    imageUrl = "https://example.com/happy.png",
                    textColor = "#333333",
                    backgroundColor = "#FFE5E5"
                ),
                emoticons = emptyList(),
                showEmoticonBottomSheet = false,
                isLoading = false,
                showExitDialog = false
            ),
            onEvent = {}
        )
    }
}

