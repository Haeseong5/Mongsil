package com.cashproject.mongsil.ui.dialog.menu

import androidx.fragment.app.Fragment
import com.cashproject.mongsil.extension.handleError

class MenuEventHandler(
    private val fragment: MenuBottomSheetDialog,
    private val viewModel: MenuViewModel,
) {
    fun handleEvent(event: MenuUiEvent) {
        when (event) {
            MenuUiEvent.Save -> {
                fragment.onSave()
            }

            MenuUiEvent.Scrap -> {
                viewModel.scrap()
            }

            MenuUiEvent.Share -> {
                fragment.onShare()
            }

            is MenuUiEvent.Error -> {
                event.throwable.handleError(fragment.context ?: return)
            }
        }
    }
}