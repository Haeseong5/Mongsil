package com.cashproject.mongsil.util

import android.util.Log.d
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private val calendarDateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA) //calendar format
    private val commentDateFormat = SimpleDateFormat("a hh시 mm분", Locale.KOREA)
    private val yearFormat = SimpleDateFormat("yyyy", Locale.KOREA)
    private val monthFormat = SimpleDateFormat("MMMMM", Locale.KOREA)
    private val dayFormat = SimpleDateFormat("dd", Locale.KOREA)
    private val todayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

    private val dateFormat = SimpleDateFormat("MMdd", Locale.KOREA)

    fun getToday(){}

    fun yearToString(date : Date) : String
        = yearFormat.format(date)

    fun dateToString(date : Date) : String
        = dateFormat.format(date)

    fun dateToTimestamp(date: Date) : Timestamp {
        val temp = todayFormat.parse(todayFormat.format(date))
        val parseDate: Date = temp!!
        val milliseconds = parseDate.time/1000
        d("DateUtil", milliseconds.toString())
        return Timestamp(parseDate.time / 1000, 0)
    }

    fun commentDateToString(date: Date): String = commentDateFormat.format(date)

    fun dateToStringArray(date: Date): Array<String> {
        return arrayOf(
            yearFormat.format(date),
            monthFormat.format(date),
            dayFormat.format(date)
        )
    }
}