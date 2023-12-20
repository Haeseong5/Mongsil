package com.cashproject.mongsil.ui.pages.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.cashproject.mongsil.ui.main.MainViewModel

@Composable
fun DiaryScreen(
    mainViewModel: MainViewModel,
    diaryViewModel: DiaryViewModel,
) {
    val context = LocalContext.current
    val uiEventHandler = remember(mainViewModel, diaryViewModel) {
        DiaryUiEventHandler(
            mainViewModel = mainViewModel,
            viewModel = diaryViewModel,
            context = context
        )
    }
    val uiState = diaryViewModel.uiState.collectAsState()

    LaunchedEffect(diaryViewModel.error) {
        diaryViewModel.error.collect {
            uiEventHandler.handleEvent(
                DiaryUiEvent.Error(it)
            )
        }
    }

    DiaryScreenContent(
        uiState = uiState.value,
        onUiEvent = uiEventHandler::handleEvent
    )
}