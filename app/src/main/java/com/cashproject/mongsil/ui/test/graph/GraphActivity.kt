package com.cashproject.mongsil.ui.test.graph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.extension.log

class GraphActivity : ComponentActivity() {

    private val transactionRepository = TransactionRepository()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            GraphScreen(
                graphModel = getGraphModel()
            )
        }
    }


    private fun getGraphModel(): GraphModel {
        val t = transactionRepository.filterTransaction().also {
            val amounts = it.map { it.amount }.sorted()
            " amounts=$amounts".log()
        }

        return GraphModel(
            xValues = listOf(1, 2, 3, 4, 5, 6, 7),
            yValues = t.map { it.amount }.sorted(), //y축
            points = t.map { it.amount }, //그래프,
            verticalStep = 2000,
        )
    }
}

// x 1..7 or 1..30
// y amount
//https://www.droidcon.com/2022/06/22/creating-a-graph-in-jetpack-compose/
@Composable
fun GraphScreen(
    graphModel: GraphModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Graph(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(500.dp),
            xValues = graphModel.xValues,
            yValues = graphModel.yValues,
            points = graphModel.points,
            paddingSpace = 16.dp,
            verticalStep = graphModel.verticalStep,
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
