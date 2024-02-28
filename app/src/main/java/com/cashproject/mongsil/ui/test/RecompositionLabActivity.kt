package com.cashproject.mongsil.ui.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.extension.log
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

@Stable
@Immutable
private data class UiState(
    val flag: Boolean,
)

@Stable
data class DateData(
    val localDateTime: LocalDateTime,
)

class RecompositionLabActivity : ComponentActivity() {

    private val uiState = MutableStateFlow(UiState(flag = false))

    private fun updateUiState() {
        uiState.value = uiState.value.copy(flag = !uiState.value.flag)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState = uiState.collectAsState()
            var flag by remember { mutableStateOf(false) }
            var localDateTime by remember {
                mutableStateOf(
                    DateData(LocalDateTime.now())
                )
            }
            val onClick = remember {
                {
                    flag = !flag
                    localDateTime = DateData(LocalDateTime.now())
                }
            }
            RecompositionLab(
                flag = flag,
                localDateTime = localDateTime,
                onClick = {
                    onClick.invoke()
                },
            )
        }
    }
}

@Composable
fun RecompositionLab(
    flag: Boolean,
    localDateTime: DateData,
    onClick: () -> Unit = {},
) {
    "recomposition $flag ${onClick.hashCode()}".log("RecompositionLab")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Column(
            modifier = Modifier
                .size(500.dp)
                .background(if (flag) Color.Blue else Color.Yellow)
        ) {
            StableItem(text = "StableItem", localDateTime = localDateTime, onClick = onClick)
        }
//        ImageItem(painter = painterResource(id = R.drawable.ic_launcher_background))
    }
}

@Composable
fun StableItem(
    text: String,
    localDateTime: DateData,
    onClick: () -> Unit = {},
) {
    "text: $localDateTime $text ${onClick.hashCode()}".log("StableItem")
    Text(
        text = text,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable {
                onClick.invoke()
            }
    )
}

@Composable
fun ImageItem(painter: Painter) {
    "painter: $painter".log("ImageItem")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        )
    }
}