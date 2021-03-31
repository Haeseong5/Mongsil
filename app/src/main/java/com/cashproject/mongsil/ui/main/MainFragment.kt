package com.cashproject.mongsil.ui.main

import android.content.Context
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentSplashBinding
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.ui.pages.calendar.CalendarFragment
import com.cashproject.mongsil.ui.pages.home.HomeFragment
import com.cashproject.mongsil.ui.pages.locker.LockerFragment
import com.cashproject.mongsil.viewmodel.CalendarViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainFragment : BaseFragment<FragmentSplashBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_splash

    override val viewModel: CalendarViewModel by viewModels {viewModelFactory}

    private lateinit var callback: OnBackPressedCallback

    private val backBtnSubject = PublishSubject.create<Boolean>() // backBtn 이벤트를 발생시킬 수 있는 Subject
    private val BACK_BTN_EXIT_TIMEOUT = 2000 // 연속된 Back 버튼의 시간 간격 (2초안에 백버튼 2번 클릭시 앱 종료)

    override fun initStartView() {
        val fragments = arrayOf<Fragment>(
            CalendarFragment(),
            HomeFragment(),
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

    override fun onResume() {
//        viewModel.getAllComments() //여기서 데이터 요청하는데, 캘린더에서 옵저브가 안됨,,
        super.onResume()
        addDisposable(backBtnSubject
            .debounce(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.v(TAG, "backBtnSubject | onNext")
            }
            .timeInterval(TimeUnit.MILLISECONDS)
            .skip(1)
            .filter { interval ->
                Log.v(TAG, "backBtnSubject | interval: $interval")
                interval.time() < BACK_BTN_EXIT_TIMEOUT
            }
            .subscribe {
//                finishActivity(0)
//                finish()
                requireActivity().finish()
            })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backBtnSubject.onNext(true)
                activity?.showToast("뒤로가기를 한번 더 누르면 앱이 종료됩니다.")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}