package com.cashproject.mongsil.extension

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Context.getNavigationBarHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    return if (Build.VERSION.SDK_INT >= 30) {
        windowManager
            .currentWindowMetrics
            .windowInsets
            .getInsets(WindowInsets.Type.navigationBars())
            .bottom

    } else {
        val currentDisplay = try {
            display
        } catch (e: NoSuchMethodError) {
            windowManager.defaultDisplay
        }

        val appUsableSize = Point()
        val realScreenSize = Point()
        currentDisplay?.apply {
            getSize(appUsableSize)
            getRealSize(realScreenSize)
        }

        // navigation bar on the side
        if (appUsableSize.x < realScreenSize.x) {
            return realScreenSize.x - appUsableSize.x
        }

        // navigation bar at the bottom
        return if (appUsableSize.y < realScreenSize.y) {
            realScreenSize.y - appUsableSize.y
        } else 0
    }
}

fun getStatusBarHeight(activity: Activity): Int {
    val rectangle = Rect()
    activity.window.decorView.getWindowVisibleDisplayFrame(rectangle)
    return rectangle.top
}

@Composable
fun getStatusBarHeight(): Dp {
    return androidx.compose.foundation.layout.WindowInsets.systemBars.asPaddingValues()
        .calculateTopPadding().value.dp
}

val getNavigationBarHeightDp
    @Composable
    get() = androidx.compose.foundation.layout.WindowInsets.navigationBars.asPaddingValues()
        .calculateTopPadding().value.dp
