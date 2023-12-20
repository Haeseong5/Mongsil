package com.cashproject.mongsil.ui.pages.detail

sealed interface DiaryUiEvent {
    data class SubmitComment(
        val content: String,
    ) : DiaryUiEvent
    data object ClickEmoticon: DiaryUiEvent

    data class Error(
        val throwable: Throwable,
    ): DiaryUiEvent
}