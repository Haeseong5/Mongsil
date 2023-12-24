package com.cashproject.mongsil.ui.pages.detail

import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.extension.handleError
import com.cashproject.mongsil.ui.dialog.emoticon.EmoticonDialog

class DiaryUiEventHandler(
    private val viewModel: DiaryViewModel,
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

            is DiaryUiEvent.TextChanged -> {
                viewModel.updateUiState {
                    copy(inputText = event.text)
                }
            }

            is DiaryUiEvent.ShowDeleteDialog -> {
                fragment.showCheckDialog(event.commentId)
            }

            is DiaryUiEvent.ShowMenuBottomSheetDialog -> {
                fragment.showMenuBottomSheetDialog(event.poster)
            }

            DiaryUiEvent.Finish -> {
                fragment.findNavController().popBackStack()
            }
        }
    }
}