package com.cashproject.mongsil.ui.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
            Column(
                modifier = Modifier
                    .width(100.dp)
                    .background(Color.Red)
            ) {
                //                Text(
//                    text = AnnotatedString("이 애플리케이션은 العربية를 지원합니다. 이 애플리케이션은 العربية를 지원합니다. 이 애플리케이션은 العربية를 지원합니다. 이 애플리케이션은 العربية를 지원합니다. 이 애플리케이션은 العربية를 지원합니다."),
//                    style = TextStyle.Default.copy(
//                        textDirection = TextDirection.Content,
//                        textAlign = TextAlign.Right
//                    ),
//                )
                @Composable
                fun TextDirectionExample() {
                    Column {
                        Text(
                            text = "This is left-to-right text",
                            modifier = Modifier.padding(16.dp)
                        )

                        Text(
                            text = "النص من اليمين إلى اليسار",
                            modifier = Modifier.padding(16.dp),
                            style = TextStyle.Default.copy(
                                textDirection = TextDirection.Content,
                                textAlign = TextAlign.Right
                            ),
                        )

                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(localeList = LocaleList("ar"))) {
                                    append("النص من اليمين إلى اليسار")
                                }
                                append("\n\n")
                                withStyle(SpanStyle(localeList = LocaleList("en"))) {
                                    append("This is left-to-right text")
                                }
                            },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                TextDirectionExample()
//                FeedPager(
//                    postList = posts.value,
//                    onPageScrolled = {},
//                    onPageSelected = {
//                        // 아랫 목록 호출 여부 결정
//                        if (posts.value.size - 3 < it) {
//                            loadMore()
//                        }
//                    },
//                    onDelete = {
//                        postList.value = postList.value.mapIndexedNotNull { index, post ->
//                            if (index == it) null
//                            else post
//                        }
//                    }
//                )
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
