package com.cashproject.mongsil.ui.pages.detail

import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.os.bundleOf
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.getImageUri
import com.cashproject.mongsil.extension.handleError
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.extension.saveImage
import com.cashproject.mongsil.extension.shareImage
import com.cashproject.mongsil.extension.showToast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun DiaryScreenContent(
    uiState: DiaryUiState = DiaryUiState(),
    sideEffect: SharedFlow<DiarySideEffect> = MutableSharedFlow(),
    onUiEvent: (DiaryUiEvent) -> Unit = {},
) {
    var commentUiVisibility by remember { mutableStateOf(true) }
    var posterBitmap: Bitmap? by remember { mutableStateOf(null) }
    val context = LocalContext.current

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
            .noRippleClickable {
                commentUiVisibility = !commentUiVisibility
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
            contentScale = ContentScale.FillHeight
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