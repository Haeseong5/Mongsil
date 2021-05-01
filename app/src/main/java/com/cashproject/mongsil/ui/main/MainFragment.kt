package com.cashproject.mongsil.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentMainBinding
import com.cashproject.mongsil.ui.pages.calendar.CalendarFragment
import com.cashproject.mongsil.ui.pages.home.HomeFragment
import com.cashproject.mongsil.ui.pages.setting.SettingFragment
import com.cashproject.mongsil.util.RxEventBus
import com.cashproject.mongsil.ui.dialog.admob.OnBackPressListener
import com.cashproject.mongsil.ui.dialog.admob.TedAdmobDialog
import com.cashproject.mongsil.ui.pages.calendar.CalendarViewModel

class MainFragment : BaseFragment<FragmentMainBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_main

    override val viewModel: CalendarViewModel by viewModels {viewModelFactory}

    private lateinit var callback: OnBackPressedCallback
    private lateinit var nativeTedAdmobDialog: TedAdmobDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdmobDialog()
    }
    override fun initStartView() {
        val fragments = arrayOf<Fragment>(
            CalendarFragment(),
            HomeFragment(),
            SettingFragment()
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


    override fun onResume() {
        super.onResume()
        RxEventBus.sendToFragment(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAdmobDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun showAdmobDialog(){
        nativeTedAdmobDialog.apply {
            setCanceledOnTouchOutside(false)
            setCancelable(true)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
            nativeTedAdmobDialog.show()
    }

    private fun initAdmobDialog(){
        nativeTedAdmobDialog =
            TedAdmobDialog.Builder(requireActivity(), TedAdmobDialog.AdType.NATIVE, getString(R.string.ad_native_id))
                .showReviewButton(true)
                .setOnBackPressListener(object : OnBackPressListener {
                    override fun onReviewClick() {
                        Log.d(TAG, "onReviewClick")
                    }
                    override fun onFinish() {
                        Log.d(TAG, "onFinish")
                        requireActivity().finish()
                    }
                    override fun onAdShow() {
                        Log.d(TAG, "onAdShow")
                        nativeTedAdmobDialog.loadNative()
                    }
                })
                .create()

        nativeTedAdmobDialog.loadNative()
        nativeTedAdmobDialog.apply {
            setCanceledOnTouchOutside(true)
            setCancelable(true)
        }
    }
}