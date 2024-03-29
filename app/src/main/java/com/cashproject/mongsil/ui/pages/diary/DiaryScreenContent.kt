package com.cashproject.mongsil.ui.pages.diary

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toBitmapOrNull
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cashproject.mongsil.extension.handleError
import com.cashproject.mongsil.extension.printErrorLog
import com.cashproject.mongsil.extension.saveImage
import com.cashproject.mongsil.extension.shareImage
import com.cashproject.mongsil.extension.showToast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
fun DiaryScreenContent(
    uiState: DiaryUiState = DiaryUiState(),
    sideEffect: SharedFlow<DiarySideEffect> = MutableSharedFlow(),
    onUiEvent: (DiaryUiEvent) -> Unit = {},
) {
    var commentUiVisibility by remember { mutableStateOf(true) }
    var posterBitmap: Bitmap? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val imeVisible = WindowInsets.isImeVisible
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    fun updateCommentUiVisibility() {
        if (imeVisible) keyboardController?.hide()
        else commentUiVisibility = !commentUiVisibility
    }

    LaunchedEffect(uiState.comments.size) {
        try {
            if (uiState.comments.isNotEmpty()) {
                listState.animateScrollToItem(0)
            }
        } catch (e: Exception) {
            e.printErrorLog()
        }
    }

    LaunchedEffect(sideEffect) {
        sideEffect.collect {
            when (it) {
                is DiarySideEffect.Error -> {
                    it.throwable.handleError(context)
                }

                DiarySideEffect.SavePoster -> {
                    try {
                        posterBitmap?.saveImage(context)
                        context.showToast("이미지가 저장되었습니다!")
                    } catch (e: Exception) {
                        e.handleError(context)
                    }
                }

                DiarySideEffect.Share -> {
                    posterBitmap.shareImage(context)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(imeVisible, commentUiVisibility) {
                detectTapGestures {
                    updateCommentUiVisibility()
                }
            }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(uiState.poster.image)
                .build(),
            onSuccess = {
                posterBitmap = it.result.drawable.toBitmapOrNull()
            },
            contentDescription = "명언 이미지",
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.FillWidth
        )

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopCenter),
            enter = fadeIn(),
            exit = fadeOut(),
            visible = commentUiVisibility
        ) {
            DiaryTopLayout(
                uiState = uiState,
                onUiEvent = onUiEvent,
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            enter = fadeIn(),
            exit = fadeOut(),
            visible = commentUiVisibility
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .consumeWindowInsets(
                        WindowInsets.navigationBars.only(WindowInsetsSides.Vertical)
                    )
                    .imePadding(),
                verticalArrangement = Arrangement.Bottom
            ) {
                CommentList(
                    modifier = Modifier
                        .weight(1f, false)
                        .pointerInput(imeVisible, commentUiVisibility) {
                            detectTapGestures {
                                updateCommentUiVisibility()
                            }
                        },
                    listState = listState,
                    comments = uiState.comments,
                    onLongClick = {
                        onUiEvent.invoke(DiaryUiEvent.ShowDeleteDialog(it))
                    }
                )
                CommentInputBox(
                    modifier = Modifier
                        .fillMaxWidth(),
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
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        DiaryScreenContent(
            uiState = DiaryUiState(
                comments = listOf(
                    Comment(),
                    Comment(),
                    Comment(),
                    Comment(),
                    Comment(),
                    Comment(),
                    Comment(),
                    Comment(),
                    Comment(),
                    Comment(),
                    Comment(),
                    Comment(),
                )
            )
        )
    }
}