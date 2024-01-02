package com.cashproject.mongsil.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Gray900 = Color(0xFF111827)
val Gray700 = Color(0xFF374151)
val Gray600 = Color(0xFF4B5563)
val Gray500 = Color(0xFF6B7280)
val Gray400 = Color(0xFF9CA3AF)
val Gray300 = Color(0xFFD1D5DB)
val Gray200 = Color(0xFFE5E7EB)
val Gray100 = Color(0xFFF3F4F6)
val Gray50 = Color(0xFFF9FAFB)
val Gray20 = Color(0xFF333333)

val black1 = Color(0xFF1E1E1E)

val primary = black1

val primaryBackgroundColor
    @Composable
    get() = if (isSystemInDarkTheme()) Color.DarkGray else Color.White

val primaryTextColor
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else black1