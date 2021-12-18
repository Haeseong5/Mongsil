package com.cashproject.mongsil.ui.main

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.main.MainFragment.Companion.PAGE_HOME
import com.cashproject.mongsil.ui.pages.home.detail.DetailFragment
import java.util.*

class ScreenSlidePagerAdapter(
    private val fragments: Array<Fragment>,
    fa: FragmentManager,
    lifecycle: Lifecycle,
    private val todaySaying: Saying
) : FragmentStateAdapter(fa, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        if (position == PAGE_HOME) {
            return fragments[position].apply {
                arguments = bundleOf(
                    "argument" to DetailFragment.Argument(
                        saying = todaySaying,
                        selectedDate = Date()
                    )
                )
            }
        }
        return fragments[position]
    }
}