package com.cashproject.mongsil.kmp.screen.calendar.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

/**
 * 년/월 계산 유틸리티
 * 기준 년월에서 offset만큼 이동한 년월을 계산합니다.
 */
fun calculateYearMonth(baseYear: Int, baseMonth: Int, offset: Int): Pair<Int, Int> {
    var year = baseYear
    var month = baseMonth + offset

    while (month > 12) {
        month -= 12
        year++
    }
    while (month < 1) {
        month += 12
        year--
    }

    return Pair(year, month)
}

/**
 * 해당 년월의 일수 계산
 */
fun getDaysInMonth(year: Int, month: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 30
    }
}

/**
 * 윤년 판별
 */
fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

/**
 * 해당 날짜가 몇 번째 요일인지 계산 (일요일 = 0)
 */
fun getStartDayOfWeek(date: LocalDate): Int {
    return when (date.dayOfWeek) {
        DayOfWeek.SUNDAY -> 0
        DayOfWeek.MONDAY -> 1
        DayOfWeek.TUESDAY -> 2
        DayOfWeek.WEDNESDAY -> 3
        DayOfWeek.THURSDAY -> 4
        DayOfWeek.FRIDAY -> 5
        DayOfWeek.SATURDAY -> 6
        else -> 0
    }
}
