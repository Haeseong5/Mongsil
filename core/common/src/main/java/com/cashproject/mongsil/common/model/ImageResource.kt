package com.cashproject.mongsil.common.model

import androidx.annotation.DrawableRes

sealed interface ImageResource {
    data class Url(val url: String) : ImageResource
    data class DrawableRes(@DrawableRes val resId: Int) : ImageResource
}
