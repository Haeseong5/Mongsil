package com.cashproject.mongsil.common.utils

import android.util.Log

fun String.log() {
    Log.d("fastLog", this)
}

fun String.errorLog() {
    Log.e("errorLog", this)
}

fun Throwable.printErrorLog(tag: String = "###", message: String = "") {
    Log.e(tag, message, this)
}
