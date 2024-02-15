package com.cashproject.mongsil.extension

import android.content.Context
import com.cashproject.mongsil.base.ScreenConfiguration


fun Context.isSmallDevice(): Boolean {
    val display = resources.displayMetrics
    val deviceWidth = display.widthPixels
    return deviceWidth.pxToDp() <= ScreenConfiguration.SMALL_DEVICE_WIDTH
}