package com.cashproject.mongsil.ui.dialog.menu

import androidx.fragment.app.Fragment
import com.cashproject.mongsil.extension.handleError

class MenuEventHandler(
    private val fragment: Fragment,
    private val viewModel: MenuViewModel,
) {
    fun handleEvent(event: MenuUiEvent) {
        when (event) {
            MenuUiEvent.Save -> {


            }

            MenuUiEvent.Scrap -> {
                viewModel.scrap()
            }

            MenuUiEvent.Share -> {

            }

            is MenuUiEvent.Error -> {
                event.throwable.handleError(fragment.context ?: return)
            }
        }
    }
}