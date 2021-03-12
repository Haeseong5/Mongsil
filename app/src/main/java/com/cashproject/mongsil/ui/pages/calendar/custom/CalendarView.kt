package com.cashproject.mongsil.ui.pages.calendar.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.ViewCalendarTopLayoutBinding
import com.cashproject.mongsil.util.CalendarUtil.convertCalendarToString
import java.text.SimpleDateFormat
import java.util.*

class CalendarView (
    context: Context
    , attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val baseCalendar: BaseCalendar = BaseCalendar()
    private lateinit var topMonthViewBinding: ViewCalendarTopLayoutBinding
    init {
        createCalendarView()
    }

    private fun createCalendarView() {
        orientation = VERTICAL
        addView(createTopMonthView())
//        addView(createTopDayView())
//        addView(createViewPager())
    }

    private fun createTopMonthView(): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        topMonthViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.view_calendar_top_layout, this, false)

        topMonthViewBinding.calendarTopTvDate.apply {
            text = convertCalendarToString(baseCalendar.calendar, monthDateFormat)
            textSize = 20F
            setOnClickListener {
                // top month text clicked
            }
        }

        topMonthViewBinding.calendarTopViewLayout.setOnClickListener {
//            refreshCurrentMonth(PREVIOUS_MONTH)
        }

        return topMonthViewBinding.root
    }

    fun refreshCurrentMonth(calendar: Calendar) {
        val sdf = SimpleDateFormat("yyyy MM", Locale.KOREAN)
        topMonthViewBinding.calendarTopTvDate.text = sdf.format(calendar.time)
    }



    companion object {
        private const val PREVIOUS_MONTH = -1
        private const val NEXT_MONTH = 1
        private const val monthDateFormat = "yyyy년 MM월"
        private val dayOfWeek = mutableListOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    }
}