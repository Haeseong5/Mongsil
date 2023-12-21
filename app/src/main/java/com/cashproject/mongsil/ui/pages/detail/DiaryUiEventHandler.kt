package com.cashproject.mongsil.ui.pages.detail

import com.cashproject.mongsil.extension.handleError
import com.cashproject.mongsil.ui.dialog.emoticon.EmoticonDialog
import com.cashproject.mongsil.ui.main.MainViewModel

class DiaryUiEventHandler(
    private val viewModel: DiaryViewModel,
    private val mainViewModel: MainViewModel,
    private val fragment: DiaryFragment,
) {
    fun handleEvent(event: DiaryUiEvent) {
        when (event) {
            DiaryUiEvent.ClickEmoticon -> {
                EmoticonDialog().apply {
                    setEmoticonBtnClickListener {
                        viewModel.updateUiState {
                            copy(
                                emoticonId = it.id
                            )
                        }
                        dismiss()
                    }
                }.show(fragment.childFragmentManager, null)
            }

            is DiaryUiEvent.SubmitComment -> {
                viewModel.submitComment(
                    content = event.content,
                )
            }

            is DiaryUiEvent.Error -> {
                event.throwable.handleError(fragment.requireContext())
            }
        }
    }
}