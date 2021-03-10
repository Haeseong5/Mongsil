package com.cashproject.mongsil.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentPagerBinding
import com.cashproject.mongsil.ui.calendar.CalendarFragment
import com.cashproject.mongsil.ui.saying.SayingFragment
import com.cashproject.mongsil.ui.locker.LockerFragment
import com.cashproject.mongsil.viewmodel.FirebaseViewModel

class PagerFragment : BaseFragment<FragmentPagerBinding, FirebaseViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_pager

    override val viewModel: FirebaseViewModel by viewModels()

    override fun initStartView() {
        val fragments = arrayOf<Fragment>(
            CalendarFragment(),
            SayingFragment(),
            LockerFragment()
        )

        /**
         * requireActivity() method will make sure that Fragment is attached and returns a valid non-null Activity which we can use without any trouble.
         */
        val screenSlidePagerAdapter = ScreenSlidePagerAdapter(
            fragments,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPager.offscreenPageLimit = fragments.size
        binding.viewPager.adapter = screenSlidePagerAdapter

    }

}