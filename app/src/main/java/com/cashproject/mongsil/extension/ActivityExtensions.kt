package com.cashproject.mongsil.extension

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager

/**
 * https://proandroiddev.com/android-full-screen-ui-with-transparent-status-bar-ef52f3adde63
 */
fun Activity.makeStatusBarTransparent() {
    window.apply {
//        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = Color.TRANSPARENT
    }
}