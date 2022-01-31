package com.cashproject.mongsil.extension

import android.util.Log

fun String.log() {
    Log.d("@@@", this)
}

fun String.errorLog() {
    Log.e("@@@", this)
}