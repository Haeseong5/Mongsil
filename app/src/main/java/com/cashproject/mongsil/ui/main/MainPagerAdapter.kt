package com.cashproject.mongsil.ui.main

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.main.MainFragment.Companion.PAGE_CALENDAR
import com.cashproject.mongsil.ui.main.MainFragment.Companion.PAGE_HOME
import com.cashproject.mongsil.ui.main.MainFragment.Companion.PAGE_LOCKER
import com.cashproject.mongsil.ui.pages.calendar.CalendarFragment
import com.cashproject.mongsil.ui.pages.detail.DetailFragment
import com.cashproject.mongsil.ui.pages.locker.LockerFragment
import java.util.*

class MainPagerAdapter(
    parentFragment: Fragment, //TODO 에러났던 원인 알아놓기
    private val todaySaying: Saying
) : FragmentStateAdapter(parentFragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_CALENDAR -> {
                CalendarFragment()
            }
            PAGE_HOME -> {
                DetailFragment().apply {
                    arguments = bundleOf(
                        "argument" to DetailFragment.Argument(
                            saying = todaySaying,
                            selectedDate = Date(),
                            from = "home"
                        )
                    )
                }
            }
            PAGE_LOCKER -> {
                LockerFragment()
            }
            else -> throw IllegalStateException()
        }
    }
}