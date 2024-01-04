package com.cashproject.mongsil.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.SuperFragment
import com.cashproject.mongsil.databinding.FragmentMainBinding
import com.cashproject.mongsil.extension.toDate
import gun0912.ted.tedadmobdialog.OnBackPressListener
import gun0912.ted.tedadmobdialog.TedAdmobDialog
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

    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

    private val mainPagerAdapter by lazy {
        MainPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            mainViewModel.getRandomSaying(
                date = LocalDate.now().toDate(),
            )
        )
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.offscreenPageLimit = 3
        binding.lifecycleOwner = viewLifecycleOwner

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.loadAllPosters()
            binding.viewPager.apply {
                adapter = mainPagerAdapter
                setCurrentItem(mainViewModel.selectedPagePosition.value, false)
            }
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

    private fun saveCurrentPagePosition() {
        try {
            mainViewModel.selectPage(binding.viewPager.currentItem)
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
        }
    }

    override fun onDestroyView() {
        saveCurrentPagePosition()
        super.onDestroyView()
        binding.viewPager.adapter = null
        _binding = null
    }
}