package com.cashproject.mongsil.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * A simple pager adapter that represents 3 ScreenSlidePageFragment objects, in
 * sequence.
 * Fragment를 사용해서 ViewPager2를 구현할 때는 FragementStateAdapter 사용
 */
class ScreenSlidePagerAdapter(
    private val fragments: Array<Fragment>,
    fa: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fa, lifecycle) {


    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}