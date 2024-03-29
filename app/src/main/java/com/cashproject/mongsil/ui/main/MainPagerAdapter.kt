package com.cashproject.mongsil.ui.main

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.ui.main.MainFragment.Companion.PAGE_CALENDAR
import com.cashproject.mongsil.ui.main.MainFragment.Companion.PAGE_HOME
import com.cashproject.mongsil.ui.main.MainFragment.Companion.PAGE_LOCKER
import com.cashproject.mongsil.ui.pages.calendar.CalendarFragment
import com.cashproject.mongsil.ui.pages.diary.DiaryFragment
import com.cashproject.mongsil.ui.pages.locker.LockerFragment
import java.util.Date

class MainPagerAdapter(
    fa: FragmentManager,
    lifecycle: Lifecycle,
    private val todayPoster: Poster
) : FragmentStateAdapter(fa, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val f = when (position) {
            PAGE_CALENDAR -> {
                CalendarFragment()
            }
            PAGE_HOME -> {
                DiaryFragment().apply {
                    arguments = bundleOf(
                        "argument" to DiaryFragment.Argument(
                            poster = todayPoster,
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
        Log.d("zzz", "+++createFragment() $position $f")
        return f
    }

}