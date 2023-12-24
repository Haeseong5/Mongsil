package com.cashproject.mongsil.ui.pages.detail

import com.cashproject.mongsil.repository.model.Poster

sealed interface DiaryUiEvent {
    data class SubmitComment(
        val content: String,
    ) : DiaryUiEvent

    data object ClickEmoticon : DiaryUiEvent

    data class ShowDeleteDialog(val commentId: Int): DiaryUiEvent

    data class TextChanged(
        val text: String,
    ) : DiaryUiEvent

    data class Error(
        val throwable: Throwable,
    ) : DiaryUiEvent

    data class ShowMenuBottomSheetDialog(val poster: Poster) : DiaryUiEvent
}