package com.cashproject.mongsil.extension

import android.content.Context
import android.widget.Toast
import com.cashproject.mongsil.R

fun Throwable.handleError(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.error_message),
        Toast.LENGTH_LONG
    ).show()
    printErrorLog()
    printStackTrace()
}