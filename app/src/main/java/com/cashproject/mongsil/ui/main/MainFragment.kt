package com.cashproject.mongsil.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.cashproject.mongsil.base.SuperFragment
import com.cashproject.mongsil.common.extensions.toDate
import com.cashproject.mongsil.databinding.FragmentMainBinding
import com.cashproject.mongsil.extension.Direction
import com.cashproject.mongsil.extension.dpToPx
import com.cashproject.mongsil.extension.handleError
import com.cashproject.mongsil.extension.openPlayStoreForReview
import com.cashproject.mongsil.extension.startFakeDrag
import com.cashproject.mongsil.ui.dialog.AdMobDialog
import com.cashproject.mongsil.util.PreferencesManager
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
                mainViewModel.handleBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.initAdLoader(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                var showExitDialog by remember { mutableStateOf(false) }
                val nativeAd by mainViewModel.nativeAd.collectAsState()
                val isAdLoading by mainViewModel.isAdLoading.collectAsState()

                LaunchedEffect(Unit) {
                    mainViewModel.showExitDialog.collect {
                        showExitDialog = true
                        mainViewModel.loadAdForExitDialog()
                    }
                }

                if (showExitDialog) {
                    AdMobDialog(
                        nativeAd = nativeAd,
                        isLoading = isAdLoading,
                        onReview = {
                            showExitDialog = false
                            openPlayStoreForReview(requireContext())
                        },
                        onClose = {
                            showExitDialog = false
                            mainViewModel.cleanupAd()
                            requireActivity().finish()

                        },
                        onDismissRequest = {
                            showExitDialog = false
                        }
                    )
                }
            }
        }

        binding.root.addView(composeView, 0, 0)

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

    override fun onDestroyView() {
        super.onDestroyView()
        firstViewCreated = false
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        binding.viewPager.adapter = null
        mainViewModel.cleanupAd()
        _binding = null
    }
}