package com.cashproject.mongsil.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable


private val defaultTypography = Typography()
val Typography
    @Composable
    get() = Typography(
        displayLarge = defaultTypography.displayLarge.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        displayMedium = defaultTypography.displayMedium.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        displaySmall = defaultTypography.displaySmall.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),

        headlineLarge = defaultTypography.headlineLarge.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        headlineMedium = defaultTypography.headlineMedium.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        headlineSmall = defaultTypography.headlineSmall.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),

        titleLarge = defaultTypography.titleLarge.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        titleMedium = defaultTypography.titleMedium.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        titleSmall = defaultTypography.titleSmall.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),

        bodyLarge = defaultTypography.bodyLarge.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        bodyMedium = defaultTypography.bodyMedium.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        bodySmall = defaultTypography.bodySmall.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),

        labelLarge = defaultTypography.labelLarge.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        labelMedium = defaultTypography.labelMedium.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
        labelSmall = defaultTypography.labelSmall.copy(
            fontFamily = regularFont,
            color = primaryTextColor
        ),
    )