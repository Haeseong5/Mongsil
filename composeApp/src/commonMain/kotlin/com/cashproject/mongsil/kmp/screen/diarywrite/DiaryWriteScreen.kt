package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.screen.diarywrite.component.DiaryContentTextField
import com.cashproject.mongsil.kmp.screen.diarywrite.component.DiaryWriteToolbar
import com.cashproject.mongsil.kmp.screen.diarywrite.component.ExitConfirmDialog
import com.cashproject.mongsil.kmp.screen.diarywrite.component.SaveButton
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteEvent
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteSideEffect
import org.koin.compose.koinInject

/**
 * 일기 작성 화면
 *
 * @param viewModel 일기 작성 ViewModel
 * @param onNavigateToCalendar 캘린더로 이동 콜백
 */
@Composable
fun DiaryWriteScreen(
    viewModel: DiaryWriteViewModel = koinInject(),
    onNavigateToCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // SideEffect 처리
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is DiaryWriteSideEffect.NavigateToCalendar -> {
                    onNavigateToCalendar()
                }
                is DiaryWriteSideEffect.NavigateBack -> {
                    onNavigateToCalendar()
                }
                is DiaryWriteSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is DiaryWriteSideEffect.ShowSaveSuccess -> {
                    snackbarHostState.showSnackbar("일기가 저장되었습니다.")
                }
            }
        }
    }

    // 시스템 뒤로가기 처리 (플랫폼별로 처리 필요)
    BackPressHandler {
        viewModel.onEvent(DiaryWriteEvent.OnBackPressed)
    }

    DiaryWriteScreenContent(
        modifier = modifier,
        year = uiState.year,
        month = uiState.month,
        day = uiState.day,
        content = uiState.content,
        isLoading = uiState.isLoading,
        showExitDialog = uiState.showExitDialog,
        snackbarHostState = snackbarHostState,
        onContentChange = { viewModel.onEvent(DiaryWriteEvent.OnContentChange(it)) },
        onSaveClick = { viewModel.onEvent(DiaryWriteEvent.OnSaveClick) },
        onBackClick = { viewModel.onEvent(DiaryWriteEvent.OnBackClick) },
        onExitConfirm = { viewModel.onEvent(DiaryWriteEvent.OnExitConfirm) },
        onExitCancel = { viewModel.onEvent(DiaryWriteEvent.OnExitCancel) }
    )
}

@Composable
private fun DiaryWriteScreenContent(
    year: Int,
    month: Int,
    day: Int,
    content: String,
    isLoading: Boolean,
    showExitDialog: Boolean,
    snackbarHostState: SnackbarHostState,
    onContentChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    onExitConfirm: () -> Unit,
    onExitCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {
            // 상단 툴바
            DiaryWriteToolbar(
                year = year,
                month = month,
                day = day,
                onBackClick = onBackClick
            )

            // 일기 내용 입력 필드
            DiaryContentTextField(
                content = content,
                onContentChange = onContentChange,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                enabled = !isLoading
            )

            // 저장 버튼
            SaveButton(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                isLoading = isLoading,
                enabled = content.isNotBlank()
            )
        }

        // 종료 확인 다이얼로그
        if (showExitDialog) {
            ExitConfirmDialog(
                onConfirm = onExitConfirm,
                onDismiss = onExitCancel
            )
        }
    }
}
