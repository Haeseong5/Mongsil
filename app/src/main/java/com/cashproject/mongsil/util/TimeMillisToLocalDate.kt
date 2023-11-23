package com.cashproject.mongsil.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Long.timeMillisToLocalDate(): LocalDate {
    val instant = Instant.ofEpochMilli(this)
    return instant.atZone(ZoneId.systemDefault()).toLocalDate()
}