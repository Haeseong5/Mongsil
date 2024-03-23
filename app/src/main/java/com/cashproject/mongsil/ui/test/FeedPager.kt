package com.cashproject.mongsil.ui.test

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import com.cashproject.mongsil.extension.log
import kotlinx.coroutines.flow.collectLatest

data class Post(
    val id: String,
    val text: String,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedPager(
    postList: List<Post>,
    onPageScrolled: (Boolean) -> Unit = {},
    onPageSelected: (Int) -> Unit = {},
    onDelete: (Int) -> Unit = {},
) {
    val pagerState = rememberPagerState {
        postList.size
    }

    val currentPage = pagerState.currentPage
    val targetPage = pagerState.targetPage
    val lastIndex = postList.size - 1
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }.collectLatest { isScrolling ->
            onPageScrolled.invoke(isScrolling)
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collectLatest { page ->
            onPageSelected.invoke(page)
        }

    }

    val context = LocalContext.current

    VerticalPager(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(rememberNestedScrollInteropConnection()),
        state = pagerState,
        beyondBoundsPageCount = 0,
        flingBehavior = PagerDefaults.flingBehavior(
            state = pagerState,
            snapAnimationSpec = tween(150, 0),
        ),
    ) {
        LaunchedEffect(key1 = Unit) {
            "### init $it".log("@@@@@")
        }
        DisposableEffect(key1 = Unit) {
            onDispose {
                "### onDispose $it".log("@@@@@")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Red, Color.Yellow, Color.Blue))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "currentPage=$currentPage\nsettledPage=${pagerState.settledPage}\ntargetPage=$targetPage\npage=$it/$lastIndex\n" +
                        postList[it].id
            )
        }
    }
}


@Composable
fun VideoPlayer() {
//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = {
//            "FeedVideoPlayer::factory --> $page".log()
//            StyledPlayerView(context).apply {
//                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
//                exoPlayer.setMediaItem(
//                    MediaItem.fromUri(
//                        AssetUrlUtil.getUrl(viewData.videoUrl)
//                    )
//                )
//                exoPlayer.playWhenReady = false
//                player = exoPlayer
//                exoPlayer.repeatMode =
//                    if (playLoop) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
//                setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
//                useController = false
//                exoPlayer.prepare()
//            }
//        },
//        update = {
//            it.playerView.player = feedPlayerManager.exoPlayer
//            it.playerView.player?.let { exoPlayer ->
//                if (pausedFromUser) {
//                    exoPlayer.playWhenReady = false
//                } else {
//                    exoPlayer.playWhenReady = isFocused
//                }
//                it.playerView.resizeMode =
//                    if (uiState.isLandscapeMode) mediaScaleOnLandScape.resizeMode else mediaScale.resizeMode
//            }
//        }
//    )
}