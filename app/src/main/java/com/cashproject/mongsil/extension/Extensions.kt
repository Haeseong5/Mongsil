package com.cashproject.mongsil.extension

import android.util.Log

fun String.log() {
    Log.d("fastLog", this)
}

fun String.errorLog() {
    Log.e("errorLog", this)
}