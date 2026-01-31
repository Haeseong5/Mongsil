package com.cashproject.mongsil.kmp.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Immutable
data class MongsilTypography(
    val default: TextStyle,
    val display1: TextStyle = default.copy(
        fontSize = 56.sp,
        lineHeight = 70.sp,
        letterSpacing = (-0.28).sp, // -0.5%
        fontWeight = FontWeight.ExtraBold
    ),
    val display2: TextStyle = default.copy(
        fontSize = 48.sp,
        lineHeight = 60.sp,
        letterSpacing = (-0.192).sp, // -0.4%
        fontWeight = FontWeight.Bold
    ),
    val title1: TextStyle = default.copy(
        fontSize = 36.sp,
        lineHeight = 46.sp,
        letterSpacing = (-0.108).sp, // -0.3%
        fontWeight = FontWeight.Bold
    ),
    val title2: TextStyle = default.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.056).sp, // -0.2%
        fontWeight = FontWeight.SemiBold
    ),
    val title3: TextStyle = default.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.024).sp, // -0.1%
        fontWeight = FontWeight.SemiBold
    ),
    val heading1: TextStyle = default.copy(
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp, // 0%
        fontWeight = FontWeight.SemiBold
    ),
    val heading2: TextStyle = default.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.04).sp, // -0.2%
        fontWeight = FontWeight.SemiBold
    ),
    val headline1: TextStyle = default.copy(
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.036).sp, // -0.2%
        fontWeight = FontWeight.SemiBold
    ),
    val headline2: TextStyle = default.copy(
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.034).sp, // -0.2%
        fontWeight = FontWeight.SemiBold
    ),
    val body1Normal: TextStyle = default.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp, // 0%
        fontWeight = FontWeight.Normal
    ),
    val body1Medium: TextStyle = default.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.032).sp, // -0.2%
        fontWeight = FontWeight.Medium
    ),
    val body1Bold: TextStyle = default.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.032).sp, // -0.2%
        fontWeight = FontWeight.Bold
    ),
    val body1Reading: TextStyle = default.copy(
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.08.sp, // 0.5%
        fontWeight = FontWeight.Normal
    ),
    val body2Normal: TextStyle = default.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.14.sp, // 1%
        fontWeight = FontWeight.Normal
    ),
    val body2Medium: TextStyle = default.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.028).sp, // -0.2%
        fontWeight = FontWeight.Medium
    ),
    val body2Bold: TextStyle = default.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.028).sp, // -0.2%
        fontWeight = FontWeight.Bold
    ),
    val body2Reading: TextStyle = default.copy(
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.14.sp, // 1%
        fontWeight = FontWeight.Normal
    ),
    val label1: TextStyle = default.copy(
        fontSize = 13.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.026.sp, // 0.2%
        fontWeight = FontWeight.SemiBold
    ),
    val label2: TextStyle = default.copy(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.036).sp, // -0.3%
        fontWeight = FontWeight.Normal
    ),
    val caption1: TextStyle = default.copy(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.06).sp, // -0.5%
        fontWeight = FontWeight.Normal
    ),
    val caption2: TextStyle = default.copy(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.055).sp, // -0.5%
        fontWeight = FontWeight.Normal
    ),
) {
    companion object {
        /**
         * 기본 FontFamily와 FontWeight로 MongsilTypography 생성
         * 객체에 Context를 입힌다는 느낌으로 with라는 이름을 붙임
         */
        fun with(
            fontFamily: FontFamily = FontFamily.Default,
            fontWeight: FontWeight = FontWeight.Normal
        ) = MongsilTypography(
            default = TextStyle(
                fontFamily = fontFamily,
                fontWeight = fontWeight
            )
        )
    }
}

internal val LocalTypography = staticCompositionLocalOf<MongsilTypography> {
    error("MongsilTypograph를 provide 해야합니다.")
}
