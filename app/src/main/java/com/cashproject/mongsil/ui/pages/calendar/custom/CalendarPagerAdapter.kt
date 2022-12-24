package com.cashproject.mongsil.ui.pages.calendar.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.viewpager.widget.PagerAdapter
import com.cashproject.mongsil.R
import com.cashproject.mongsil.data.db.entity.Comment
import com.cashproject.mongsil.model.data.Day

import java.util.*

class CalendarPagerAdapter(private val context: Context) : PagerAdapter() {

    companion object {
        const val NUMBER_OF_PAGES = 12 * 10
    }

    private var viewContainer: ViewGroup? = null
    private var onDayClickListener: ((Calendar, Day) -> Unit)? = null

    fun setOnDayClickListener(listener: ((Calendar, Day) -> Unit)) {
        this.onDayClickListener = listener
    }

    fun setList(list: List<Comment>) {
        val views = viewContainer ?: return
        (0 until views.childCount).forEach { i ->
            ((views.getChildAt(i) as GridView).adapter as? CalendarGridAdapter)?.updateList(list)
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val gridView = GridView(context)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        gridView.apply {
            overScrollMode = GridView.OVER_SCROLL_NEVER
            layoutParams = params
            numColumns = 7
            adapter = CalendarGridAdapter(context, getCalendar(position))
            setSelector(R.drawable.calendar_list_selector)
            setOnItemClickListener { adapterView, _, pos, _ ->
                val selectedDay = adapterView.getItemAtPosition(pos) as Day
                onDayClickListener?.invoke(getCalendar(position), selectedDay)
            }
        }

        container.addView(gridView)
        viewContainer = container

        return gridView
    }

    override fun getCount(): Int = NUMBER_OF_PAGES

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean = (view == `object`)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    private fun getCalendar(position: Int): Calendar {
        return Calendar.getInstance().apply {
            add(Calendar.MONTH, position - NUMBER_OF_PAGES / 2)
        }
    }
}