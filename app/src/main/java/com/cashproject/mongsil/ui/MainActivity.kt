package com.cashproject.mongsil.ui

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseActivity
import com.cashproject.mongsil.databinding.ActivityMainBinding
import com.cashproject.mongsil.ui.calendar.CalendarFragment
import com.cashproject.mongsil.ui.home.HomeFragment
import com.cashproject.mongsil.ui.locker.LockerFragment
import com.cashproject.mongsil.ui.main.ScreenSlidePagerAdapter


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main


}