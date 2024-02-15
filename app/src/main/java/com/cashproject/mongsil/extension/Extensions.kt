package com.cashproject.mongsil.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.os.bundleOf
import com.cashproject.mongsil.base.ScreenConfiguration

fun String.log() {
    Log.d("fastLog", this)
}

fun String.errorLog() {
    Log.e("errorLog", this)
}

fun openPlayStore(context: Context) {
    val packageName: String = context.packageName
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    } catch (anfe: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun Throwable.printErrorLog(tag: String = "###", message: String = "") {
    Log.e(tag, message, this)
}

fun Map<String, Any?>.toBundle(): Bundle = bundleOf(*this.toList().toTypedArray())


@Composable
fun isSmallWidthDevice(): Boolean {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    return screenWidth <= ScreenConfiguration.SMALL_DEVICE_WIDTH
}
