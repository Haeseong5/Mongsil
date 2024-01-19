package com.cashproject.mongsil.extension

import androidx.compose.ui.graphics.Color

fun String.toColor(): Color {
    return try {
        if (first() != '#') {
            Color(android.graphics.Color.parseColor("#$this"))
        } else {
            Color(android.graphics.Color.parseColor(this))
        }
    } catch (e: Exception) {
        e.printErrorLog()
        Color.Unspecified
    }
}