package com.cashproject.mongsil.ui.composables.calendar.data

import java.time.DayOfWeek

// E.g DayOfWeek.SATURDAY.daysUntil(DayOfWeek.TUESDAY) = 3
fun DayOfWeek.daysUntil(other: DayOfWeek) = (7 + (other.value - value)) % 7
