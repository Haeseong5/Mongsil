package com.cashproject.mongsil.ui.pages.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.cashproject.mongsil.extension.noRippleClickable

@Composable
fun DiaryScreenContent(
    uiState: DiaryUiState = DiaryUiState(),
    onUiEvent: (DiaryUiEvent) -> Unit = {},
) {
    var commentUiVisibility by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable {
                commentUiVisibility = !commentUiVisibility
            }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = uiState.poster.image,
            contentDescription = "명언 이미지",
            contentScale = ContentScale.FillHeight
        )

        DiaryTopLayout(
            modifier = Modifier.align(Alignment.TopCenter),
            uiState = uiState,
            onUiEvent = onUiEvent,
        )

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(),
            exit = fadeOut(),
            visible = commentUiVisibility
        ) {
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
                    .noRippleClickable { }
            ) {
                CommentList(
                    modifier = Modifier.fillMaxHeight(0.6f),
                    comments = uiState.comments,
                    onLongClick = {
                        onUiEvent.invoke(DiaryUiEvent.ShowDeleteDialog(it))
                    }
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
}

@Preview
@Composable
private fun Preview() {
    DiaryScreenContent(
        uiState = DiaryUiState()
    )
}