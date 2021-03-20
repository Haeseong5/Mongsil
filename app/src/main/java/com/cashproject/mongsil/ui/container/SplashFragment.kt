package com.cashproject.mongsil.ui.container

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentSplashBinding
import com.cashproject.mongsil.ui.pages.calendar.ListFragment
import com.cashproject.mongsil.ui.pages.saying.SayingFragment
import com.cashproject.mongsil.ui.pages.locker.LockerFragment
import com.cashproject.mongsil.viewmodel.CalendarViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import java.util.logging.Handler

class SplashFragment : BaseFragment<FragmentSplashBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_splash

    override val viewModel: CalendarViewModel by viewModels()

    private lateinit var callback: OnBackPressedCallback

    private val backBtnSubject = PublishSubject.create<Boolean>() // backBtn 이벤트를 발생시킬 수 있는 Subject
    private val BACK_BTN_EXIT_TIMEOUT = 2000 // 연속된 Back 버튼의 시간 간격 (2초안에 백버튼 2번 클릭시 앱 종료)

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

    override fun onResume() {
        super.onResume()
        addDisposable(backBtnSubject
            .debounce(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.v(TAG, "backBtnSubject | onNext")
                Toast.makeText(requireActivity(), "Please press back once more to exit", Toast.LENGTH_SHORT)
                    .show()
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
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}