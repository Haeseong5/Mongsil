package com.cashproject.mongsil.ui.pages.detail

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
}