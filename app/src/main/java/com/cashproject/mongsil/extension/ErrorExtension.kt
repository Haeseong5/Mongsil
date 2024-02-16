package com.cashproject.mongsil.extension

import android.content.Context
import android.widget.Toast
import com.cashproject.mongsil.R
import com.cashproject.mongsil.common.utils.printErrorLog

fun Throwable.handleError(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.error_message),
        Toast.LENGTH_LONG
    ).show()
    printErrorLog()
    printStackTrace()
}

fun Context.showToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}