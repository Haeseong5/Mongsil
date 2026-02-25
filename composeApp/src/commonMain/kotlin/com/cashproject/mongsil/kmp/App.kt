package com.cashproject.mongsil.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.di.appModules
import com.cashproject.mongsil.kmp.screen.main.MainScreen
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.gamja_flower_regular
import org.jetbrains.compose.resources.Font
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.KoinApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

@Composable
fun App(
    fontFamily: FontFamily = FontFamily(Font(resource = Res.font.gamja_flower_regular)),
    onDarkThemeChange: ((Boolean) -> Unit)? = null,
    appViewModel: AppViewModel = koinViewModel(),
) {
    val appUiState by appViewModel.uiState.collectAsStateWithLifecycle()

    if (onDarkThemeChange != null) {
        LaunchedEffect(appUiState.isDarkTheme) { onDarkThemeChange(appUiState.isDarkTheme) }
    }

    MongsilTheme(
        darkTheme = appUiState.isDarkTheme,
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
