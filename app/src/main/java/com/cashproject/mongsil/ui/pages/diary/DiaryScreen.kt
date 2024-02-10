package com.cashproject.mongsil.ui.pages.diary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@Composable
fun DiaryScreen(
    diaryViewModel: DiaryViewModel,
    onUiEvent: (DiaryUiEvent) -> Unit = {}
) {
    val uiState by diaryViewModel.uiState.collectAsState()
    val sideEffect = diaryViewModel.sideEffect

    DiaryScreenContent(
        uiState = uiState,
        sideEffect = sideEffect,
        onUiEvent = onUiEvent
    )
}