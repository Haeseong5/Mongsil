package com.cashproject.mongsil.ui.dialog.menu

import com.cashproject.mongsil.extension.handleError

class MenuEventHandler(
    private val fragment: MenuBottomSheetDialog,
    private val viewModel: MenuViewModel,
    private val dismissOnClick: Boolean,
) {
    fun handleEvent(event: MenuUiEvent) {
        when (event) {
            MenuUiEvent.Save -> {
                fragment.onSave()
                if (dismissOnClick) fragment.dismiss()
            }

            MenuUiEvent.Scrap -> {
                viewModel.scrap()
                if (dismissOnClick) fragment.dismiss()
            }

            MenuUiEvent.Share -> {
                fragment.onShare()
                if (dismissOnClick) fragment.dismiss()
            }

            is MenuUiEvent.Error -> {
                event.throwable.handleError(fragment.context ?: return)
            }
        }
    }
}