package com.cashproject.mongsil.ui.pages.diary

import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.ui.pages.diary.dialog.EmoticonSelectionBottomSheetDialogFragment

class DiaryUiEventHandler(
    private val viewModel: DiaryViewModel,
    private val fragment: DiaryFragment,
    private val mainViewModel: MainViewModel,
) {
    fun handleEvent(event: DiaryUiEvent) {
        when (event) {
            DiaryUiEvent.ClickEmoticon -> {
                EmoticonSelectionBottomSheetDialogFragment(
                    emoticons = viewModel.uiState.value.emoticons,
                    onClickItem = {
                        viewModel.updateUiState {
                            copy(
                                emoticonId = it.id
                            )
                        }
                    }
                ).show(fragment.childFragmentManager, null)
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

            DiaryUiEvent.LoadedPoster -> {
                if (viewModel.isPagerItem) mainViewModel.emitPagerTutorialAnimEvent()
            }
        }
    }
}