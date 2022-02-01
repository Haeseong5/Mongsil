package com.cashproject.mongsil.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentMainBinding
import com.cashproject.mongsil.ui.dialog.admob.OnBackPressListener
import com.cashproject.mongsil.ui.dialog.admob.TedAdmobDialog
import java.util.*

class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    companion object {
        const val PAGE_CALENDAR = 0
        const val PAGE_HOME = 1
        const val PAGE_LOCKER = 2
    }

//    override val layoutResourceId: Int
//        get() = R.layout.fragment_main

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var callback: OnBackPressedCallback
    private lateinit var nativeTedAdmobDialog: TedAdmobDialog

    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

//    private val mainPagerAdapter by lazy {
//        MainPagerAdapter(
//            parentFragment = this,
//            todaySaying = mainViewModel.getRandomSaying(Date())
//        )
//    }

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
//        Log.d(this.javaClass.name, "mainPagerAdapter address: $mainPagerAdapter")
        binding.viewPager.adapter = MainPagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
            todaySaying = mainViewModel.getRandomSaying(Date()) ?: return
        )

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
        binding.viewPager.adapter = null
        _binding = null
    }
}