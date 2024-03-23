package com.cashproject.mongsil.ui.test.graph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class GraphActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GraphScreen()
        }
    }
}

@Composable
fun GraphScreen() {
    val yStep = 50
    val random = Random
    /* to test with random points */
    val points = (0..9).map {
        var num = random.nextInt(350)
        if (num <= 50)
            num += 100
        num.toFloat()
    }
    /* to test with fixed points */
//                val points = listOf(150f,100f,250f,200f,330f,300f,90f,120f,285f,199f),
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Graph(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            xValues = (0..9).map { it + 1 },
            yValues = (0..6).map { (it + 1) * yStep },
            points = points,
            paddingSpace = 16.dp,
            verticalStep = yStep,
            graphAppearance = GraphAppearance(
                Color.White,
                MaterialTheme.colors.primary,
                1f,
                true,
                Color.Green,
                false,
                MaterialTheme.colors.secondary,
                MaterialTheme.colors.background
            )
        )
    }
}
