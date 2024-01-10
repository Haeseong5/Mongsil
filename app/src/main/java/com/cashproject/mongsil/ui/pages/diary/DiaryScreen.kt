package com.cashproject.mongsil.ui.pages.diary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

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