package com.cashproject.mongsil.ui.pages.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage

@Composable
fun DiaryScreenContent(
    uiState: DiaryUiState = DiaryUiState(),
    onUiEvent: (DiaryUiEvent) -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = uiState.posterUrl,
            contentDescription = "명언 이미지",
            contentScale = ContentScale.FillHeight
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f)
                        )
                    )
                )
                .align(Alignment.BottomCenter),
        ) {
            CommentList(
                modifier = Modifier.fillMaxHeight(0.7f),
                comments = uiState.comments,
            )
            CommentInputBox(
                modifier = Modifier.fillMaxWidth(),
                emoticonId = uiState.emoticonId,
                text = uiState.inputText,
                onValueChange = {
                    onUiEvent.invoke(DiaryUiEvent.TextChanged(it))
                },
                onClickEmoticon = {
                    onUiEvent.invoke(DiaryUiEvent.ClickEmoticon)
                },
                onConfirm = {
                    onUiEvent.invoke(DiaryUiEvent.SubmitComment(uiState.inputText))
                    onUiEvent.invoke(DiaryUiEvent.TextChanged(""))
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    DiaryScreenContent(
        uiState = DiaryUiState()
    )
}