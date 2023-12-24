package com.cashproject.mongsil.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class DateFormat(val pattern: String) {
    YearMonthDayAndTime("yyyy-MM-dd HH:mm:ss"),
    YearMonthDay("yyyy-MM-dd"),
    MonthDay("MM-dd"),
    PlainMonthDay("MMdd"),
    HourMinute("HH:mm"),
    YEAR("yyyy"),
}

fun Date.toTextFormat(
    format: DateFormat = DateFormat.YearMonthDayAndTime
): String {
    val dateFormat = SimpleDateFormat(format.pattern, Locale.getDefault())
    return dateFormat.format(this)
}