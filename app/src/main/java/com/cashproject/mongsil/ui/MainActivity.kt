package com.cashproject.mongsil.ui

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseActivity
import com.cashproject.mongsil.databinding.ActivityMainBinding
import com.cashproject.mongsil.ui.calendar.CalendarFragment
import com.cashproject.mongsil.ui.home.HomeFragment
import com.cashproject.mongsil.ui.locker.LockerFragment

private const val NUM_PAGES = 3

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    private val pagerAdapter by lazy {
        ScreenSlidePagerAdapter(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewPager()
    }

    private fun initViewPager(){
        binding.mainViewPager.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        if (binding.mainViewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            binding.mainViewPager.currentItem = binding.mainViewPager.currentItem - 1
        }
    }

    /**
     * A simple pager adapter that represents 3 ScreenSlidePageFragment objects, in
     * sequence.
     * Fragment를 사용해서 ViewPager2를 구현할 때는 FragementStateAdapter 사용
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            d(TAG, position.toString())

            when (position){
                0 -> return HomeFragment()
                1 -> return CalendarFragment()
                else -> return LockerFragment()
            }

        }
    }
}