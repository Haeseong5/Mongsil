package com.cashproject.mongsil.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.SuperFragment
import com.cashproject.mongsil.databinding.FragmentMainBinding
import com.cashproject.mongsil.extension.Direction
import com.cashproject.mongsil.extension.dpToPx
import com.cashproject.mongsil.extension.handleError
import com.cashproject.mongsil.extension.startFakeDrag
import com.cashproject.mongsil.extension.toDate
import com.cashproject.mongsil.util.PreferencesManager
import gun0912.ted.tedadmobdialog.OnBackPressListener
import gun0912.ted.tedadmobdialog.TedAdmobDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainFragment : SuperFragment() {

    companion object {
        const val PAGE_CALENDAR = 0
        const val PAGE_HOME = 1
        const val PAGE_LOCKER = 2
    }

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var callback: OnBackPressedCallback
    private lateinit var nativeTedAdmobDialog: TedAdmobDialog

    private var firstViewCreated: Boolean = true

    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

    private val mainPagerAdapter by lazy {
        MainPagerAdapter(
            fa = childFragmentManager,
            lifecycle = lifecycle,
            todayPoster = mainViewModel.getRandomSaying(
                date = LocalDate.now().toDate(),
            )
        )
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            mainViewModel.currentPage.tryEmit(position)
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdmobDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.allPosters.collect {
                if (it.isNotEmpty() && binding.viewPager.adapter == null) {
                    initPager()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mainViewModel.error.collect {
                    it.handleError(requireContext())
                }
            }
        }

        observePagerTutorialAnimEvent()
    }

    private fun observePagerTutorialAnimEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.showPagerTutorialAnim.collect {
                startPagerTutorialAnim()
            }
        }
    }

    private suspend fun startPagerTutorialAnim() {
        if (!PreferencesManager.isTutorialAnimationViewed && firstViewCreated) {
            delay(500)
            binding.viewPager.startFakeDrag(
                duration = 400L,
                direction = Direction.START,
                pxToMove = 50.dpToPx(),
            )
            delay(1000)
            binding.viewPager.startFakeDrag(
                duration = 400L,
                direction = Direction.END,
                pxToMove = 50.dpToPx(),
            )
            PreferencesManager.isTutorialAnimationViewed = true
        }
    }

    private fun initPager() {
        binding.viewPager.apply {
            adapter = mainPagerAdapter
            registerOnPageChangeCallback(onPageChangeCallback)
            setCurrentItem(mainViewModel.currentPage.value, false)
            binding.viewPager.offscreenPageLimit = 3
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun showAdmobDialog() {
        nativeTedAdmobDialog.apply {
            setCanceledOnTouchOutside(false)
            setCancelable(true)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        nativeTedAdmobDialog.show()
    }

    private fun initAdmobDialog() {
        nativeTedAdmobDialog =
            TedAdmobDialog.Builder(
                requireActivity(),
                TedAdmobDialog.AdType.NATIVE,
                getString(R.string.ad_native_id)
            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        firstViewCreated = false
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        binding.viewPager.adapter = null
        _binding = null
    }
}