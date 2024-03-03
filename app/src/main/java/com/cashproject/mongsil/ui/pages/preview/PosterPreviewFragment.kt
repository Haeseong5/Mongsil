package com.cashproject.mongsil.ui.pages.preview

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.SuperFragment
import com.cashproject.mongsil.extension.getStatusBarHeight
import com.cashproject.mongsil.extension.handleError
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.extension.saveImage
import com.cashproject.mongsil.extension.shareImage
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.manager.showInterstitialAd
import com.cashproject.mongsil.ui.dialog.menu.MenuBottomSheetDialog
import com.cashproject.mongsil.ui.pages.diary.model.Poster
import com.cashproject.mongsil.ui.pages.diary.model.findPosition
import com.cashproject.mongsil.ui.pages.locker.LockerViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.util.Date

class PosterPreviewFragment : SuperFragment() {

    private val viewModel: LockerViewModel by viewModels()
    private var mInterstitialAd: InterstitialAd? = null

    companion object {
        fun start(
            fragment: Fragment,
            posterId: String,
            navOptions: NavOptions? = null
        ) {
            try {
                fragment.findNavController().navigate(
                    resId = R.id.posterPreviewFragment,
                    args = bundleOf("posterId" to posterId),
                    navOptions = navOptions
                )
            } catch (e: Exception) {
                Log.e("tag", e.stackTraceToString())
            }
        }
    }

    private val posterId get() = arguments?.getString("posterId") ?: ""
    private val sideEffect: MutableSharedFlow<PosterPreviewSideEffect> =
        MutableSharedFlow(extraBufferCapacity = 1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState = viewModel.uiState.collectAsState()
                val posters = uiState.value.posters
                PosterPreviewScreen(
                    posters = posters,
                    initialPage = posters.findPosition(posterId),
                    onFinish = {
                        findNavController().popBackStack()
                    },
                    sideEffect = sideEffect,
                    onShowMenuDialog = ::showMenuBottomSheetDialog
                )

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInterstitialAd()
    }

    private fun showMenuBottomSheetDialog(poster: Poster) {
        MenuBottomSheetDialog(
            poster = poster,
            selectedDate = Date(),
            dismissOnClick = true,
        ).apply {
            onSave = {
                showInterstitialAd(mInterstitialAd, requireActivity())
                sideEffect.tryEmit(PosterPreviewSideEffect.Save)
            }
            onShare = {
                sideEffect.tryEmit(PosterPreviewSideEffect.Share)
            }
        }.show(childFragmentManager, "approval")
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PosterPreviewScreen(
    initialPage: Int,
    posters: List<Poster>,
    sideEffect: SharedFlow<PosterPreviewSideEffect>,
    onFinish: () -> Unit,
    onShowMenuDialog: (Poster) -> Unit,
) {
    val context = LocalContext.current
    val statusBarHeightDp = getStatusBarHeight()
    var posterBitmap: Bitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = sideEffect, block = {
        sideEffect.collect {
            when (it) {
                PosterPreviewSideEffect.Save -> {
                    try {
                        posterBitmap?.saveImage(context)
                        context.showToast("이미지가 저장되었습니다!")
                    } catch (e: Exception) {
                        e.handleError(context)
                    }
                }

                PosterPreviewSideEffect.Share -> {
                    posterBitmap.shareImage(context)
                }
            }
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (posters.isNotEmpty()) {
            val pagerState = rememberPagerState(initialPage = initialPage) {
                posters.size
            }
            VerticalPager(
                state = pagerState
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .noRippleClickable {
                            onShowMenuDialog.invoke(posters[it])
                        },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(posters[it].image)
                        .build(),
                    onSuccess = {
                        posterBitmap = it.result.drawable.toBitmapOrNull()
                    },
                    contentDescription = "명언 이미지",
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.FillWidth
                )
            }
        }

        Image(
            modifier = Modifier
                .padding(top = statusBarHeightDp)
                .padding(start = 16.dp)
                .size(30.dp)
                .align(Alignment.TopStart)
                .noRippleClickable {
                    onFinish.invoke()
                },
            painter = rememberVectorPainter(image = Icons.Outlined.Close),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = "info"
        )
    }
}