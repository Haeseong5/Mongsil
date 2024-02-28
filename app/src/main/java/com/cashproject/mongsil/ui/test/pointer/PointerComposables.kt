package com.cashproject.mongsil.ui.test.pointer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalDragOrCancellation
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.extension.log
import kotlin.math.roundToInt

//https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/foundation/foundation/samples/src/main/java/androidx/compose/foundation/samples/ScrollerSamples.kt

@Composable
fun SampleVerticalList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            item {
                DetectHorizontalDragGesturesSample()
            }
            item {
                HorizontalDragSample()
            }
            item {
                AwaitHorizontalDragOrCancellationSample()
            }
            item {
                DraggableSample()
            }
            item {
                Draggable2DSample()
            }
        }
    )
}

@Composable
fun DetectHorizontalDragGesturesSample() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var boxSize by remember { mutableIntStateOf(0) }
    val circleSizePx = 200
    val circleSizeDp = with(LocalDensity.current) { 200.toDp() }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .aspectRatio(1f)
            .background(Color.Cyan)
            .onSizeChanged { boxSize = it.width }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    "dragAmount=$dragAmount change=$change".log()
                    change.consume()
                    offsetX = (change.position.x - (circleSizePx / 2))
                    offsetY = (change.position.y - (circleSizePx / 2))
                }
            }
    ) {
        Box(
            modifier = Modifier
                .offset(
                    x = with(LocalDensity.current) { offsetX.toDp() },
                    y = with(LocalDensity.current) { offsetY.toDp() }
                )
                .size(circleSizeDp)
                .background(Color.Blue, CircleShape)
        )
    }
}

@Composable
fun HorizontalDragSample() {
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    var width by remember { mutableFloatStateOf(0f) }
    val circleSizePx = 200
    val circleSizeDp = with(LocalDensity.current) { 200.toDp() }

    Box(
        Modifier
            .padding(10.dp)
            .aspectRatio(1f)
            .background(Color.LightGray)
            .onSizeChanged { width = it.width.toFloat() }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    val change = awaitHorizontalTouchSlopOrCancellation(down.id) { change, over ->
                        "OnHorizontalTouchSlopReached --> change=$change over=$over".log()
                        change.consume()
                        offsetX.floatValue = (change.position.x - (circleSizePx / 2))
                        offsetY.floatValue = (change.position.y - (circleSizePx / 2))
                    }
                    if (change != null) {
                        horizontalDrag(change.id) {
                            "horizontalDrag::onDrag --> ${it.positionChange()} ${it.position}".log()
                            it.consume()
                            offsetX.floatValue = (it.position.x - (circleSizePx / 2))
                            offsetY.floatValue = (it.position.y - (circleSizePx / 2))
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .offset(
                    x = with(LocalDensity.current) { offsetX.floatValue.toDp() },
                    y = with(LocalDensity.current) { offsetY.floatValue.toDp() }
                )
                .size(circleSizeDp)
                .background(Color.Blue, CircleShape)
        )
    }
}


@Composable
fun AwaitHorizontalDragOrCancellationSample() {
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    var width by remember { mutableFloatStateOf(0f) }
    val circleSizePx = 200
    val circleSizeDp = with(LocalDensity.current) { 200.toDp() }

    Box(
        Modifier
            .padding(10.dp)
            .aspectRatio(1f)
            .background(Color.Magenta)
            .onSizeChanged { width = it.width.toFloat() }
            .pointerInput(Unit) {
                awaitEachGesture {
                    "awaitEachGesture".log() //suspend 함수가 없으면 무한 호출
                    val down = awaitFirstDown()
                    var change =
                        awaitHorizontalTouchSlopOrCancellation(down.id) { change, over ->
                            offsetX.floatValue = (change.position.x - (circleSizePx / 2))
                            offsetY.floatValue = (change.position.y - (circleSizePx / 2))
                            change.consume()
                        }
                    while (change != null && change.pressed) {
                        change = awaitHorizontalDragOrCancellation(change.id)
                        if (change != null && change.pressed) {
                            offsetX.floatValue = (change.position.x - (circleSizePx / 2))
                            offsetY.floatValue = (change.position.y - (circleSizePx / 2))
                            change.consume()
                        }
                    }
                }
            }
    ) {
        Box(
            Modifier
                .offset {
                    IntOffset(
                        offsetX.floatValue.roundToInt(),
                        offsetY.floatValue.roundToInt()
                    )
                }
                .size(circleSizeDp)
                .background(Color.Blue, CircleShape)
        )
    }
}

@Composable
fun DraggableSample() {
    // Draw a seekbar-like composable that has a black background
    // with a red square that moves along the 300.dp drag distance
    val max = 300.dp
    val min = 0.dp
    val (minPx, maxPx) = with(LocalDensity.current) { min.toPx() to max.toPx() }
    // this is the  state we will update while dragging
    val offsetPosition = remember { mutableStateOf(0f) }

    // seekbar itself
    Box(
        modifier = Modifier
            .width(max)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val newValue = offsetPosition.value + delta
                    offsetPosition.value = newValue.coerceIn(minPx, maxPx)
                }
            )
            .background(Color.Black)
    ) {
        Box(
            Modifier
                .offset { IntOffset(offsetPosition.value.roundToInt(), 0) }
                .size(50.dp)
                .background(Color.Red)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Draggable2DSample() {
    // Draw a box that has a a grey background
    // with a red square that moves along 300.dp dragging in both directions
    val max = 200.dp
    val min = 0.dp
    val (minPx, maxPx) = with(LocalDensity.current) { min.toPx() to max.toPx() }
    // this is the offset we will update while dragging
    var offsetPositionX by remember { mutableStateOf(0f) }
    var offsetPositionY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .width(max)
            .height(max)
            .draggable2D(
                state = rememberDraggable2DState { delta ->
                    delta
                        .toString()
                        .log()
                    val newValueX = offsetPositionX + delta.x
                    val newValueY = offsetPositionY + delta.y
                    offsetPositionX = newValueX.coerceIn(minPx, maxPx)
                    offsetPositionY = newValueY.coerceIn(minPx, maxPx)
                }
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset {
                    IntOffset(
                        offsetPositionX.roundToInt(),
                        offsetPositionY.roundToInt()
                    )
                }
                .size(50.dp)
                .background(Color.Red)
        )
    }
}