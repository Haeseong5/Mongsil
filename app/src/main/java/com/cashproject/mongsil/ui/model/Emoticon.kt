package com.cashproject.mongsil.ui.model

import androidx.compose.ui.graphics.Color


data class Emoticon(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val textColor: Color,
    val backgroundColor: Color,
)


//enum class Type {
//    FREE, CASH, AD,
//}