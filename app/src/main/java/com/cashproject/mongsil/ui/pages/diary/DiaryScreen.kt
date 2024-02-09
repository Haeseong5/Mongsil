package com.cashproject.mongsil.ui.pages.diary

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    diaryViewModel: DiaryViewModel,
    onUiEvent: (DiaryUiEvent) -> Unit = {}
) {
    val uiState by diaryViewModel.uiState.collectAsState()
    val sideEffect = diaryViewModel.sideEffect
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()
    val listState = rememberLazyGridState()

    DiaryScreenContent(
        uiState = uiState,
        sideEffect = sideEffect,
        onUiEvent = onUiEvent
    )

    if (uiState.isVisibleEmoticonSelectionBottomSheet) {
        EmoticonSelectionBottomSheet(
            listState = listState,
            sheetState = sheetState,
            emoticons = uiState.emoticons,
            onDismissRequest = {
                diaryViewModel.updateUiState {
                    copy(
                        isVisibleEmoticonSelectionBottomSheet = false
                    )
                }
            },
            onClickItem = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    diaryViewModel.updateUiState {
                        copy(
                            isVisibleEmoticonSelectionBottomSheet = false
                        )
                    }
                }
            }
        )
    }

}