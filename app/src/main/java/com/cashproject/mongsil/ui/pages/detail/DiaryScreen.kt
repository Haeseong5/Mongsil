package com.cashproject.mongsil.ui.pages.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.cashproject.mongsil.extension.handleError

@Composable
fun DiaryScreen(
    diaryViewModel: DiaryViewModel,
    onUiEvent: (DiaryUiEvent) -> Unit = {}
) {
    val uiState = diaryViewModel.uiState.collectAsState()
    val sideEffect = diaryViewModel.sideEffect
    DiaryScreenContent(
        uiState = uiState.value,
        sideEffect = sideEffect,
        onUiEvent = onUiEvent
    )
}