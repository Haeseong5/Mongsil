package com.cashproject.mongsil.ui.pages.preview

sealed interface PosterPreviewSideEffect {
    data object Save : PosterPreviewSideEffect
    data object Share : PosterPreviewSideEffect
}