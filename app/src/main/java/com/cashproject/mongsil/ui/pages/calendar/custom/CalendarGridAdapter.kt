package com.cashproject.mongsil.ui.pages.calendar.custom

import android.content.Context
import android.util.Log
import android.util.Log.v
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.cashproject.mongsil.R
import com.cashproject.mongsil.data.db.entity.Comment
import com.cashproject.mongsil.model.data.Day
import com.cashproject.mongsil.model.data.Emoticons.emoticons
import com.cashproject.mongsil.util.CalendarUtil
import java.util.*

class CalendarGridAdapter(private val context: Context, private val calendar: Calendar) :
    BaseAdapter() {

    companion object {
        private const val SIZE_OF_DAY = 7 * 6
        private const val SUNDAY = 0
        private const val SATURDAY = 6
        private val reviewList = mutableListOf<Comment>()
        private val TAG = this::class.java.simpleName
    }

    private val dayList = mutableListOf<Day>()
    private val month = calendar.get(Calendar.MONTH)

    init {
        setCalendar()
    }

    fun updateList(list: List<Comment>) {
        reviewList.apply {
            clear()
            addAll(list)
        }
        setCalendar()
//        notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView = view ?: layoutInflater.inflate(R.layout.view_calendar_day_layout, null)

        val dayTv: TextView = mView.findViewById(R.id.day_tv)
        val backLayout: FrameLayout = mView.findViewById(R.id.back_layout)

        val posterIv: ImageView = mView.findViewById(R.id.poster_iv)
        val todayCv: CardView = mView.findViewById(R.id.cv_today)

        val day: Day = getItem(position) as Day

        val itemMonth = day.calendar.get(Calendar.MONTH)
        val itemDay = day.calendar.get(Calendar.DATE)

        dayTv.apply {
            text = itemDay.toString()
            when (position % 7) {
                SUNDAY -> setTextColor(ContextCompat.getColor(context, R.color.sunday))
                SATURDAY -> setTextColor(ContextCompat.getColor(context, R.color.saturday))
            }
        }

        val alphaValue = if (itemMonth != month) 0.4F else 1.0F
        dayTv.alpha = alphaValue
        posterIv.alpha = alphaValue

        day.calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

//        d("DayTime", "${itemMonth}, $itemDay")
//        d("DayTime", day.calendar.timeInMillis.toString())
//        d("DayTime", today.toString())

        if (day.calendar.timeInMillis == today){
            v("Today", "TRUE")
            todayCv.visibility = View.VISIBLE
        }

        if (day.comments.isNotEmpty()) {
            posterIv.visibility = View.VISIBLE
            dayTv.visibility = View.GONE
            val lastIndex = day.comments.lastIndex
            posterIv.setImageResource(emoticons[day.comments[lastIndex].emotion].icon)
        } else {
            posterIv.visibility = View.GONE
            dayTv.visibility = View.VISIBLE
        }

        return mView
    }

    override fun getItem(position: Int): Any = dayList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = dayList.size

    private fun setCalendar() {
        Log.d(TAG, "setCalendar()")
        dayList.clear()

        val cal = calendar.clone() as Calendar
        cal.apply {
            set(Calendar.DATE, 1)
            val startOfMonth = cal.get(Calendar.DAY_OF_WEEK) - 1
            add(Calendar.DATE, -startOfMonth)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        while (dayList.size < SIZE_OF_DAY) {
            val it = reviewList.iterator()
            val reviews = mutableListOf<Comment>()
            while (it.hasNext()) {
                val item = it.next()
                if (CalendarUtil.isCalendarAndDateSame(cal, item.date)) {
                    reviews.add(item)
                }
            }
            dayList.add(Day(cal.clone() as Calendar, reviews))
            cal.add(Calendar.DATE, 1)
        }
        notifyDataSetChanged()
    }
}