package com.cashproject.mongsil.ui.pages.calendar.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log.d
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.ViewCalendarTopLayoutBinding
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Day
import com.cashproject.mongsil.util.CalendarUtil.convertCalendarToString
import com.cashproject.mongsil.util.CalendarUtil.isMonthSame
import java.util.*

internal class CalendarView(
    context: Context
    , attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val baseCalendar = Calendar.getInstance()
    private val calendarViewPager: CalendarViewPager by lazy { CalendarViewPager(context) }
    private val calendarPagerAdapter: CalendarPagerAdapter by lazy { CalendarPagerAdapter(context) }

    private var onDayClickListener: ((Day) -> Unit)? = null
    private lateinit var topMonthViewBinding: ViewCalendarTopLayoutBinding
    private val layoutParams :LayoutParams by lazy {
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
    }
    init {
        createCalendarView()
    }

    private fun createCalendarView() {
        orientation = VERTICAL
        setLayoutParams(layoutParams)
        gravity = Gravity.CENTER
        addView(createTopMonthView())
        addView(createTopDayView())
        addView(createViewPager())

        requestLayout()
    }

    fun setOnDayClickListener(listener: ((Day) -> Unit)) {
        this.onDayClickListener = listener
    }

    fun notifyDataChanged(list: List<Comment>) {
        calendarPagerAdapter.setList(list)
    }

    //2021년 4월
    private fun createTopMonthView(): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        topMonthViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.view_calendar_top_layout, this, false)

        topMonthViewBinding.dateTv.apply {
            text = convertCalendarToString(baseCalendar, monthDateFormat)
            textSize = 20F
            setOnClickListener {
                // top month text clicked
            }
        }

        topMonthViewBinding.preBtn.setOnClickListener {
            setViewPagerPosition(PREVIOUS_MONTH)
        }
        topMonthViewBinding.nextBtn.setOnClickListener {
            setViewPagerPosition(NEXT_MONTH)
        }

        return topMonthViewBinding.root
    }

    //월 화 수 목 금 토 일 레이아웃
    private fun createTopDayView(): LinearLayout {
        val linearLayout = LinearLayout(context)
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 10, 0, 10)
        }
        linearLayout.apply {
            orientation = HORIZONTAL
            layoutParams = params
            weightSum = 7F
            setBackgroundColor(Color.TRANSPARENT)
        }
        for (element in dayOfWeek) { //dayOfWekk: 7
            linearLayout.addView(createTextView(element))
        }
        linearLayout.requestLayout()
        return linearLayout
    }

    //월 화 수 목 금 토 일 텍스트 삽입
    private fun createTextView(string: String): TextView {
        val params = LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1F
        )
        val textView = TextView(context).apply {
            text = string
            textSize = 14F
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            layoutParams = params
            setBackgroundResource(android.R.color.transparent)
        }
        return textView
    }

    private fun createViewPager(): CalendarViewPager {
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER_VERTICAL
        }

        //날짜 클릭 리스너
        calendarPagerAdapter.setOnDayClickListener { calendar, day -> // 파라미터 이해!
            //클릭한 날짜가 이번달 날짜가 아니라면
            if (!isMonthSame(day.calendar, calendar)) {
                when (day.calendar.compareTo(calendar)) {
                    PREVIOUS_MONTH -> setViewPagerPosition(PREVIOUS_MONTH) //이전 달 날짜이면, 이전 달력 표시
                    NEXT_MONTH -> setViewPagerPosition(NEXT_MONTH) //다음 달 날짜이면, 다음 달 달력 표시
                }
            }
            onDayClickListener?.invoke(day) //클릭 콜백 호출
        }

        calendarViewPager.apply {
            this.layoutParams = layoutParams //ViewPager match_parent/wrap_content 로 지정
            adapter = calendarPagerAdapter //뷰페이저 데이터 없는 어댑터 설정
            setOnPageSelectedListener { position -> //페이지 변경됐을 때 호출됨.
                setTopMonthText(position) //년월 변경
            }
        }

        return calendarViewPager
    }

    private fun setViewPagerPosition(offset: Int) {
        val newPosition = calendarViewPager.currentItem + offset
        calendarViewPager.currentItem = newPosition
        setTopMonthText(newPosition)
    }

    private fun setTopMonthText(viewPagerPosition: Int) {
        val calendar = (baseCalendar.clone() as Calendar).apply {
            add(Calendar.MONTH, viewPagerPosition - CalendarPagerAdapter.NUMBER_OF_PAGES / 2)
        }
        topMonthViewBinding.dateTv.text = convertCalendarToString(calendar, monthDateFormat)
    }

    companion object {
        private const val PREVIOUS_MONTH = -1
        private const val NEXT_MONTH = 1
        private const val monthDateFormat = "yyyy년 MM월"
        private val dayOfWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    }
}