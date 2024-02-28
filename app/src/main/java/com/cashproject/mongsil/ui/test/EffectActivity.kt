package com.cashproject.mongsil.ui.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.extension.log

class EffectActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EffectScreen()
        }
    }
}

@Composable
fun ListScreen() {
    val list = remember {
        mutableStateOf(
            listOf(
                "A",
                "B",
                "C",
                "A",
                "B",
                "C",
                "A",
                "B",
                "C",
                "A",
                "B",
                "C",
                "A",
                "B",
                "C",
                "A",
                "B",
                "C",
                "A",
                "B",
                "C",
            )
        )
    }
    val listState = rememberLazyListState()

    val isEnabled by remember { derivedStateOf { listState.isScrolledToEnd(1) } } //listState.firstVisibleItemIndex > 0 값이 바뀔 때만 리컴포지션 됨.
//    val isEnabled = listState.firstVisibleItemIndex > 0 //listState.firstVisibleItemIndex 이 바뀔 때마다 리컴포지션 됨.
//    val isEnabled by remember(listState.firstVisibleItemIndex) { mutableStateOf(listState.isScrolledToEnd(1)) } //listState.firstVisibleItemIndex이 바뀔 때마다 리컴포지션 됨.


    isEnabled.toString().log()
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState
        ) {
            items(list.value) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    text = it,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(if (isEnabled) Color.Red else Color.Gray),
        )
    }
}

@Composable
fun TodoList(highPriorityKeywords: List<String> = listOf("Review", "Unblock", "Compose")) {

    val todoTasks =
        remember { mutableStateListOf<String>("Review", "Unblock", "Study", "Exercise", "Block") }

    // 이 코드는 highPriorityKeywords가 todoTasks에 비해 훨씬 덜 자주 변경된다고 가정합니다
    val highPriorityTasks by remember(highPriorityKeywords) {
        derivedStateOf { todoTasks.filter { highPriorityKeywords.contains(it) } }
    }
    val normalPriorityTasks by remember(highPriorityKeywords) {
        derivedStateOf { todoTasks - highPriorityTasks.toSet() }
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            items(highPriorityTasks) {
                HighPriorityTask(it)
            }
            items(normalPriorityTasks) {
                NormalTask(it)
            }
        }
    }
}

@Composable
fun HighPriorityTask(task: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp)
            .background(Color.Red)
    ) {
        Text(text = task)
    }
}

@Composable
fun NormalTask(task: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp)
            .background(Color.Yellow)
    ) {
        Text(text = task)
    }
}


@Composable
fun EffectScreen(

) {
    "recomposition EffectScreen".log()
    val list = remember { mutableStateListOf("A", "B") }
    var input by remember { mutableStateOf("A") }

    fun onClick() {
        input = if (input != list[0]) list[0] else list[1]
    }

    fun expensiveCalculation(input: String): String {
        var result = ""
        for (i in 0..10) {
            result += "$input "
        }
        return result
    }

    val output = remember() { derivedStateOf { expensiveCalculation(input) } }

    Column {
        Text(text = output.value)
        Box(
            modifier = Modifier
                .size(128.dp)
                .background(Color.Red)
                .clickable(onClick = ::onClick)
        )
    }
}


fun LazyListState.isScrolledToEnd(extra: Int = 5): Boolean {
    "isScrolledToEnd".log()
    val index = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return false
    return index >= layoutInfo.totalItemsCount - extra
}