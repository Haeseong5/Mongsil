package com.cashproject.mongsil.ui.pages.calendar.custom

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cashproject.mongsil.R
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Day
import com.cashproject.mongsil.ui.emoticon.Emoticons.emoticons
import com.cashproject.mongsil.util.CalendarUtil

import java.util.*

class CalendarGridAdapter(private val context: Context, private val calendar: Calendar) :
    BaseAdapter() {

    companion object {
        private const val SIZE_OF_DAY = 7 * 6
        private const val SUNDAY = 0
        private const val SATURDAY = 6
        private val reviewList = mutableListOf<Comment>()
    }

    private val dayList = mutableListOf<Day>()
    private val month = calendar.get(Calendar.MONTH)

    init {
        setCalendar()
    }

    fun setList(list: List<Comment>) {
        reviewList.apply {
            clear()
            addAll(list)
        }
        setCalendar()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView = view ?: layoutInflater.inflate(R.layout.view_calendar_day_layout, null)

        val dayTv: TextView = mView.findViewById(R.id.day_tv)
        val posterIv: ImageView = mView.findViewById(R.id.poster_iv)

        val day: Day = getItem(position) as Day

        val itemMonth = day.calendar.get(Calendar.MONTH)
        val itemDay = day.calendar.get(Calendar.DATE)

        dayTv.apply {
            text = itemDay.toString()
            when (position % 7) {
                SUNDAY -> setTextColor(ContextCompat.getColor(context, R.color.sunday))
                SATURDAY -> setTextColor(ContextCompat.getColor(context, R.color.saturday))
                else -> setTextColor(Color.BLACK)
            }
        }

        val alphaValue = if (itemMonth != month) 0.4F else 1.0F
        dayTv.alpha = alphaValue
        posterIv.alpha = alphaValue

        if (day.comments.isNotEmpty()) {
            posterIv.visibility = View.VISIBLE
            val lastIndex = day.comments.lastIndex
            posterIv.setImageResource(emoticons[day.comments[lastIndex].emotion].icon)
        } else {
            posterIv.visibility = View.GONE
        }

        return mView
    }

    override fun getItem(position: Int): Any = dayList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = dayList.size

    private fun setCalendar() {
        dayList.clear()

        val cal = calendar.clone() as Calendar
        cal.apply {
            set(Calendar.DATE, 1)
            val startOfMonth = cal.get(Calendar.DAY_OF_WEEK) - 1
            add(Calendar.DATE, -startOfMonth)
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
    }
}