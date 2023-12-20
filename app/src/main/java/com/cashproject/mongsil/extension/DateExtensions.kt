package com.cashproject.mongsil.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class DateFormat(val pattern: String) {
    YearMonthDayAndTime("yyyy-MM-dd HH:mm:ss"),
    YearMonthDay("yyyy-MM-dd"),
    MonthDay("MM-dd"),
    HourMinute("HH:mm"),
}

fun Date.toTextFormat(
    format: DateFormat = DateFormat.YearMonthDayAndTime
): String {
    val dateFormat = SimpleDateFormat(format.pattern, Locale.getDefault())
    return dateFormat.format(this)
}