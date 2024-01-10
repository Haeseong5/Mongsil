package com.cashproject.mongsil.ui.pages.diary

sealed interface DiarySideEffect {
    data class Error(val throwable: Throwable) : DiarySideEffect
    data object SavePoster : DiarySideEffect
    data object Share : DiarySideEffect
}