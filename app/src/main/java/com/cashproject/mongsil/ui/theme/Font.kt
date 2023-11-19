package com.cashproject.mongsil.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.R

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

@Composable
fun pxToDp(pixels: Float) = with(LocalDensity.current) { pixels.toDp() }


val gamjaflowerFamily = FontFamily(
    Font(R.font.gamjaflower_regular, FontWeight(500), FontStyle.Normal)
)

val latoTextStyle: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = gamjaflowerFamily,
        fontSize = dpToSp(18.dp),
        fontWeight = FontWeight(500),
        color = Color.Black
    )