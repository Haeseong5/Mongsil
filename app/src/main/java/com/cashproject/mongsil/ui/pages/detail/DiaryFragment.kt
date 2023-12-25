package com.cashproject.mongsil.ui.pages.detail

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.R
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.extension.DateFormat
import com.cashproject.mongsil.extension.getStatusBarHeight
import com.cashproject.mongsil.extension.toTextFormat
import com.cashproject.mongsil.manager.showInterstitialAd
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.ui.dialog.CheckDialog
import com.cashproject.mongsil.ui.dialog.menu.MenuBottomSheetDialog
import com.cashproject.mongsil.util.PermissionUtil.hasWriteStoragePermission
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.parcelize.Parcelize
import java.util.Date


class DiaryFragment : Fragment() {

    companion object {
        fun start(
            fragment: Fragment,
            argument: Argument,
            navOptions: NavOptions? = null
        ) {
            try {
                fragment.findNavController().navigate(
                    resId = R.id.action_global_detailFragment,
                    args = bundleOf("argument" to argument),
                    navOptions = navOptions
                )
            } catch (e: Exception) {
                Log.e("tag", e.stackTraceToString())
            }
        }
    }

    @Parcelize
    data class Argument(
        val poster: Poster,
        val selectedDate: Date,
        val from: String? = null
    ) : Parcelable

    private val argument: Argument by lazy {
        arguments?.getParcelable<Argument>("argument")
            ?: throw IllegalArgumentException("Argument must exist")
    }

    private val diaryViewModel: DiaryViewModel by viewModels {
        DiaryViewModel.createViewModelFactory(
            date = argument.selectedDate,
            isPagerItem = argument.from == "home"
        )
    }

    private val uiEventHandler: DiaryUiEventHandler by lazy {
        DiaryUiEventHandler(
            viewModel = diaryViewModel,
            fragment = this,
        )
    }

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInterstitialAd()
        hasWriteStoragePermission(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DiaryScreen(
                    diaryViewModel = diaryViewModel,
                    onUiEvent = uiEventHandler::handleEvent
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DiaryFragment", argument.selectedDate.toTextFormat(DateFormat.YearMonthDayAndTime))
    }

    fun showMenuBottomSheetDialog(poster: Poster) {
        MenuBottomSheetDialog(
            poster = poster,
            selectedDate = argument.selectedDate
        ).apply {
            onSave = {
                showInterstitialAd(mInterstitialAd, requireActivity())
                diaryViewModel.emitSideEffect(DiarySideEffect.SavePoster)
            }
            onShare = {
                diaryViewModel.emitSideEffect(DiarySideEffect.Share)
            }
        }.show(childFragmentManager, "approval")
    }

    fun showCheckDialog(id: Int) {
        CheckDialog(
            context = requireContext(),
            accept = {
                diaryViewModel.deleteCommentById(id)
            }
        ).also {
            it.start(getString(R.string.message_delete))
        }
    }

    private fun initInterstitialAd() {
        val adRequest: AdRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            requireContext(),
            requireContext().getString(R.string.ad_interstitial_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    Log.i("TAG", "onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.i("TAG", loadAdError.message)
                    mInterstitialAd = null
                }
            }
        )
    }
}

