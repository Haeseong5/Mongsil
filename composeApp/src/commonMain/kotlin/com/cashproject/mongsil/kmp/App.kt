package com.cashproject.mongsil.kmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.di.appModules
import com.cashproject.mongsil.kmp.screen.main.MainScreen
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.gamja_flower_regular
import mongsil.composeapp.generated.resources.poppins_medium
import org.jetbrains.compose.resources.Font
import org.koin.core.KoinApplication
import org.koin.dsl.KoinAppDeclaration

@Composable
fun App(
    fontFamily: FontFamily = FontFamily(Font(resource = Res.font.gamja_flower_regular)),
    onDarkThemeChange: ((Boolean) -> Unit)? = null,
) {
    // TODO 다크모드 상태 collect 코드 추가

    MongsilTheme(
        darkTheme = false, // TODO 추가,
        fontFamily = fontFamily
    ) {
        MainScreen()
    }
}

internal fun mongsilAppDeclaration(
    additionalDeclaration: KoinApplication.() -> Unit = {},
): KoinAppDeclaration = {
    modules(appModules)
    additionalDeclaration()
}

@Composable
expect fun getPlatformName(): String
