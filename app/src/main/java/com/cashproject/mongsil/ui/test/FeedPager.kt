package com.cashproject.mongsil.ui.test

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.extension.log
import com.cashproject.mongsil.extension.showToast
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
        beyondBoundsPageCount = 1,
        flingBehavior = PagerDefaults.flingBehavior(
            state = pagerState,
            snapAnimationSpec = tween(150, 0),
//            snapVelocityThreshold = 5.dp,
//            lowVelocityAnimationSpec = tween(easing = LinearEasing, durationMillis = 10),
//            snapAnimationSpec = spring(stiffness = 100f),
//            snapPositionalThreshold = 0.35f
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Red, Color.Yellow, Color.Blue))),
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue)
//                    .pointerInput(Unit) {
//                        awaitEachGesture {
//                            "awaitEachGesture".log("pointer")
//                            awaitFirstDown(pass = PointerEventPass.Initial).also { it.consume() }
//                            "awaitFirstDown".log("pointer")
//                            val up = waitForUpOrCancellation()
//                            "up".log("pointer")
//
//                            if (up != null) {
//                                up.consume()
//                                context.showToast("clicked")
//                            }
//                        }
//                    }
                    .clickable {
                        context.showToast("clicked")
                        "clicked".log()
                    }
            )
            Text(
                text = "currentPage=$currentPage, settledPage=${pagerState.settledPage} targetPage=$targetPage, page=$it/$lastIndex " +
                        postList[it].id
            )

            Text(
                modifier = Modifier
//                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .clickable {
                        onDelete.invoke(it)
                    },
                text = "Delete"
            )
        }
        ("currentPage=$currentPage, targetPage=$targetPage, settledPage=${pagerState.settledPage}, page=$it/$lastIndex " + postList[it].id).log(
            "@@@@@"
        )
    }
}