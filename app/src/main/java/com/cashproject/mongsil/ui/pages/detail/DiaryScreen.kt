package com.cashproject.mongsil.ui.pages.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.cashproject.mongsil.ui.main.MainViewModel

@Composable
fun DiaryScreen(
    diaryViewModel: DiaryViewModel,
    onUiEvent: (DiaryUiEvent) -> Unit = {}
) {
    val uiState = diaryViewModel.uiState.collectAsState()

    LaunchedEffect(diaryViewModel.error) {
        diaryViewModel.error.collect {
            onUiEvent(DiaryUiEvent.Error(it))
        }
    }

    DiaryScreenContent(
        uiState = uiState.value,
        onUiEvent = onUiEvent
    )
}