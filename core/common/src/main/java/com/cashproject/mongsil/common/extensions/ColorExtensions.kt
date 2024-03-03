package com.cashproject.mongsil.common.extensions


import androidx.compose.ui.graphics.Color
import com.cashproject.mongsil.common.utils.printErrorLog

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
