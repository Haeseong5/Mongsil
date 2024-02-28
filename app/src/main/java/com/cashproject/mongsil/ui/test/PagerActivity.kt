package com.cashproject.mongsil.ui.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalDragOrCancellation
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.cashproject.mongsil.extension.log
import com.cashproject.mongsil.ui.test.pointer.SampleVerticalList
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
            SampleVerticalList()


            val modifier = Modifier
                .padding(16.dp)
                .also {
                    it
                        .toString()
                        .log("1")
                }
            modifier
                .size(100.dp)
                .also {
                    it
                        .toString()
                        .log("2")
                }
//            DetectHorizontalDragGesturesSample()
//            AwaitHorizontalDragOrCancellationSample()
//            Draggable2DSample()
//            val posts = postList.collectAsState()
//            Box(modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Red)) {
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
//            }
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


@Composable
fun AwaitHorizontalDragOrCancellationSample23() {
    val offsetX = remember { mutableStateOf(0f) }
    offsetX.toString().log()
//    val offsetY = remember { mutableStateOf(0f) }
//    var width by remember { mutableStateOf(0f) }
    Box(
        Modifier.fillMaxSize()
    ) {
        Box(
            Modifier
//                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .fillMaxHeight()
                .width(50.dp)
                .background(Color.Blue)
//                .pointerInput(onRoomClick) {
//                    detectTapGestures {
//                        onRoomClick()
//                    }
//                }
//                .pointerInput(onRoomClick) {
//                    detectHorizontalDragGestures { change, dragAmount ->
//                        runOnGLThread {
//                            val handler = NativeProxyCharacter.handler ?: return@runOnGLThread
//                            val currentAngle = handler.localEulerAngles
//                                ?: return@runOnGLThread
//                            handler.setLocalEulerAngles(
//                                x = currentAngle.getOrElse(0) { 0f },
//                                y = currentAngle.getOrElse(1) { 0f } - dragAmount / 12 * 3.5f,
//                                z = currentAngle.getOrElse(2) { 0f })
//                        }
//                        change.consume()
//                    }
//                }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        var change =
                            /**
                             *
                             * 수평 드래그 동작이 터치 슬롭을 통과하기를 기다리며, pointerId를 조사하는 데 사용됩니다. 만약 pointerId가 해제되면, 내려간 포인터 중 다른 포인터가 제스처를 이끌도록 선택되고, 만약 아무 것도 내려가 있지 않으면 null이 반환됩니다.
                             *
                             * onTouchSlopReached는 ViewConfiguration.touchSlop 이후의 수평 방향 모션에서 호출되며, 모션을 초과하는 변화와 터치 슬롭을 초과하는 픽셀이 포함된다. onTouchSlopReached는 모션을 수용하면서 위치 변경을 소비해야 합니다. 모션을 수락하면 해당 PointerInputChange가 반환됩니다. 그렇지 않으면 터치 슬롭 감지가 계속됩니다. awaitHorizontalTouchSlopOrCancellation이 호출될 때 pointerId가 내려가지 않았다면 null이 반환됩니다.
                             *
                             * 반환값:
                             * onTouchSlopReached에서 소비된 PointerInputChange 또는 터치 슬롭이 감지되기 전에 모든 포인터가 해제되었거나 다른 제스처가 위치 변경을 소비한 경우 null이 반환됩니다.
                             *
                             */
                            awaitHorizontalTouchSlopOrCancellation(down.id) { change, over ->
                                "change=$change over=$over".log()
                                val originalX = offsetX.value
                                val newValue = (originalX + over)
                                /**
                                 *
                                 * 변경 이벤트를 소비하고 해당 변경 정보를 호출자에게 모두 요청합니다. 일반적으로 버튼이 클릭될 때 "up" 이벤트를 소비하여 이 버튼의 다른 부모가 이 "up"을 다시 소비할 수 없게 하는 경우에 필요합니다.
                                 *
                                 * "소비"는 단순히 요청을 나타내는 것이며 각 포인터 입력 핸들러 구현은 이 플래그를 수동으로 확인하여 존중해야 합니다.
                                 */
                                change.consume()
                                offsetX.value = newValue
                            }
                        while (change != null && change.pressed) {
                            /**
                             * 수평 드래그가 감지되거나 모든 포인터가 올라갈 때까지 포인터 입력 이벤트를 읽습니다. 마지막 포인터가 올라가면 해당 up 이벤트가 반환됩니다. 드래그 이벤트가 감지되면 드래그 변경이 반환됩니다. pointerId가 해제되면 사용 가능한 경우 다른 내려간 포인터가 사용되므로 반환된 PointerInputChange.id는 pointerId와 다를 수 있습니다. 만약 위치 변경이 PointerEventPass.Main 패스에 의해 소비되었다면 드래그는 취소된 것으로 간주되어 null이 반환됩니다. awaitHorizontalDragOrCancellation이 호출될 때 pointerId가 내려가지 않았다면 null이 반환됩니다.
                             */
                            change = awaitHorizontalDragOrCancellation(change.id)
                            if (change != null && change.pressed) {
                                val originalX = offsetX.value
                                val newValue = (originalX + change.positionChange().x)
                                change.consume()
                                offsetX.value = newValue
                            }
                        }
                    }
                }
        )
    }
}