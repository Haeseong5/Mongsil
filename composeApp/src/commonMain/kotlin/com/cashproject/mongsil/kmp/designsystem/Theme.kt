package com.cashproject.mongsil.kmp.designsystem


import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontFamily

val LocalDarkTheme = compositionLocalOf { true }


@Composable
fun MongsilTheme(
    fontFamily: FontFamily = FontFamily.Default,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalColorScheme provides if (darkTheme) {
            MongsilColorScheme.darkColorScheme
        } else {
            MongsilColorScheme.lightColorScheme
        },
        LocalIndication provides ripple(),
        LocalTypography provides MongsilTypography.with(fontFamily),
//        LocalAsyncImagePreviewHandler provides AsyncImagePreviewHandler {
//            ColorImage(Color.Red.toArgb())
//        }, TODO coil 라이브러리 사용 시
        content = content
    )


}

object MongsilTheme {
    val colorScheme: MongsilColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: MongsilTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}


val LocalColorScheme = staticCompositionLocalOf { MongsilColorScheme.lightColorScheme }