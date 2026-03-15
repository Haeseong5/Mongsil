package com.cashproject.mongsil.kmp.designsystem.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.kmp.designsystem.LocalAppFontScale

@Composable
fun fixedScaleTextStyle(fontStyle: TextStyle): TextStyle {
    val systemFontScale = LocalDensity.current.fontScale
    val appFontScale = LocalAppFontScale.current
    val totalScale = systemFontScale * appFontScale

    if (totalScale == 1f) return fontStyle

    return fontStyle.copy(
        fontSize = fontStyle.fontSize.fixedSp(totalScale),
        lineHeight = fontStyle.lineHeight.fixedSp(totalScale),
        letterSpacing = fontStyle.letterSpacing.fixedSp(totalScale),
    )
}

private fun TextUnit.fixedSp(totalScale: Float): TextUnit {
    if (!isSpecified || type != TextUnitType.Sp) return this
    return (value / totalScale).sp
}
