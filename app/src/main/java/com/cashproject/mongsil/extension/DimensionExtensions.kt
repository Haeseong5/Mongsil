package com.cashproject.mongsil.extension

import android.content.res.Resources

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Float.dpToPx(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Int.pxToDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Float.pxToDp(): Float = (this / Resources.getSystem().displayMetrics.density)