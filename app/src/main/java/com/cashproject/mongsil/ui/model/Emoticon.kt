package com.cashproject.mongsil.ui.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class Emoticon(
    val id: Int,
    val emotion: String, //TODO change string resource
    @DrawableRes val icon: Int,
    @ColorRes val textColor: Int,
    @ColorRes val background: Int
)