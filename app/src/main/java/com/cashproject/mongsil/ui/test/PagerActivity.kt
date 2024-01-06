package com.cashproject.mongsil.ui.test

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random


fun generateRandomString(length: Int = 10): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { charset[Random.nextInt(0, charset.length)] }
        .joinToString("")
}

fun load(): Post {
    val text = generateRandomString()
    return Post(id = text, text)
}

class PagerActivity : ComponentActivity() {

    private val postList: MutableStateFlow<List<Post>> = MutableStateFlow(listOf(load()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle", "++onCreate")
        val initList = buildList {
            for (i in 0..10) {
                add(load())
            }
        }

        lifecycleScope.launch {
            postList.emit(initList)
        }

        setContent {
            val posts = postList.collectAsState()
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)) {
                FeedPager(
                    postList = posts.value,
                    onPageScrolled = {},
                    onPageSelected = {
                        // 아랫 목록 호출 여부 결정
                        if (posts.value.size - 3 < it) {
                            loadMore()
                        }
                    },
                    onDelete = {
                        postList.value = postList.value.mapIndexedNotNull { index, post ->
                            if (index == it) null
                            else post
                        }
                    }
                )
            }
        }
    }
    private fun loadMore() {
        lifecycleScope.launch {
            val newList = buildList {
                for (i in 0..10) {
                    add(load())
                }
            }

            postList.emit(
                postList.value + newList
            )
        }
    }
}