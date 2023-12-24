package com.cashproject.mongsil.ui.dialog.menu

sealed interface MenuUiEvent {
    data object Share : MenuUiEvent
    data object Scrap : MenuUiEvent
    data object Save : MenuUiEvent

    data class Error(val throwable: Throwable) : MenuUiEvent
}