package com.cashproject.mongsil.ui.pages.diary

import com.cashproject.mongsil.ui.pages.diary.model.Poster


sealed interface DiaryUiEvent {
    data class SubmitComment(
        val content: String,
    ) : DiaryUiEvent

    data object ClickEmoticon : DiaryUiEvent

    data class ShowDeleteDialog(val commentId: Int) : DiaryUiEvent

    data class TextChanged(
        val text: String,
    ) : DiaryUiEvent

    data class ShowMenuBottomSheetDialog(val poster: Poster) : DiaryUiEvent

    data object Finish : DiaryUiEvent

    data object LoadedPoster : DiaryUiEvent

    data object ClickTopLayoutEmoticon : DiaryUiEvent
}