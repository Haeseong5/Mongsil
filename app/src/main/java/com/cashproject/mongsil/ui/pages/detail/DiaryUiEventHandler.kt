package com.cashproject.mongsil.ui.pages.detail

import android.content.Context
import com.cashproject.mongsil.extension.handleError
import com.cashproject.mongsil.ui.main.MainViewModel

class DiaryUiEventHandler(
    private val viewModel: DiaryViewModel,
    private val mainViewModel: MainViewModel,
    private val context: Context,
) {
    fun handleEvent(event: DiaryUiEvent) {
        when (event) {
            DiaryUiEvent.ClickEmoticon -> {

            }

            is DiaryUiEvent.SubmitComment -> {
                viewModel.submitComment(
                    content = event.content,
                )
            }

            is DiaryUiEvent.Error -> {
                event.throwable.handleError(context)
            }
        }
    }
}