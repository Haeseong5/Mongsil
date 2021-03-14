package com.cashproject.mongsil.ui.container

import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentSplashBinding
import com.cashproject.mongsil.ui.pages.calendar.ListFragment
import com.cashproject.mongsil.ui.pages.saying.SayingFragment
import com.cashproject.mongsil.ui.pages.locker.LockerFragment
import com.cashproject.mongsil.viewmodel.CalendarViewModel
import java.util.logging.Handler

class SplashFragment : BaseFragment<FragmentSplashBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_splash

    override val viewModel: CalendarViewModel by viewModels()

    override fun initStartView() {
        val fragments = arrayOf<Fragment>(
            ListFragment(),
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