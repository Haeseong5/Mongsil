package com.cashproject.mongsil.ui.pages.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.model.Emoticons

@Composable
fun DiaryScreenContent(
    uiState: DiaryUiState = DiaryUiState(),
    onUiEvent: (DiaryUiEvent) -> Unit = {},
) {
    var inputComment by remember { mutableStateOf("") }

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
                .align(Alignment.BottomCenter),
        ) {
            CommentInputBox(
                modifier = Modifier.fillMaxWidth(),
                emoticonId = uiState.emoticonId,
                inputComment = {
                    inputComment = it
                },
                onClickEmoticon = {
                    onUiEvent.invoke(DiaryUiEvent.ClickEmoticon)
                },
                onConfirm = {
                    onUiEvent.invoke(DiaryUiEvent.SubmitComment(inputComment))
                }
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            content = {
                items(uiState.comments) {
                    Text(text = it.content)
                }
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    DiaryScreenContent(
        uiState = DiaryUiState()
    )
}