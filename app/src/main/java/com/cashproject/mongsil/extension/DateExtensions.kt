package com.cashproject.mongsil.extension

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
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

fun LocalDate.toDate(): Date {
    val instant = atStartOfDay(ZoneId.systemDefault()).toInstant()
    return Date.from(instant)
}

fun Date.toLocalDate(): LocalDate {
    val date = this
    val instant = date.toInstant()
    val zonedDateTime = instant.atZone(ZoneId.systemDefault())
    return zonedDateTime.toLocalDate()
}

fun Date.excludeTimeFromDate(): Date {
    val inputDate = this
    // Create a Calendar instance and set its time to the inputDate
    val calendar = Calendar.getInstance()
    calendar.time = inputDate

    // Set the time fields (hour, minute, second) to zero
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Get the modified Date object
    return calendar.time
}

fun Long.timeMillisToLocalDate(): LocalDate {
    val instant = Instant.ofEpochMilli(this)
    return instant.atZone(ZoneId.systemDefault()).toLocalDate()
}