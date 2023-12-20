package com.cashproject.mongsil.ui.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

    VerticalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
//        key = {
//            postList[it].id
//        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "currentPage=$currentPage, targetPage=$targetPage, page=$it/$lastIndex " +
                        postList[it].id
            )

            Text(
                modifier = Modifier.padding(16.dp).clickable {
                    onDelete.invoke(it)
                },
                text = "Delete"
            )
        }
    }
}