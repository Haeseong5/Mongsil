package com.cashproject.mongsil.kmp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.di.appModules
import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ThemeMode
import com.cashproject.mongsil.kmp.screen.main.MainScreen
import com.cashproject.mongsil.kmp.screen.setting.screenlock.AppLockGate
import com.cashproject.mongsil.kmp.screen.setting.screenlock.AppLockViewModel
import com.cashproject.mongsil.kmp.screen.setting.screenlock.NativeScreenLockAuthenticator
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.gamja_flower_regular
import org.jetbrains.compose.resources.Font
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.KoinApplication
import org.koin.dsl.KoinAppDeclaration

@Composable
fun App(
    onDarkThemeChange: ((Boolean) -> Unit)? = null,
    appViewModel: AppViewModel = koinViewModel(),
) {
    val appUiState by appViewModel.uiState.collectAsStateWithLifecycle()
    val appLockViewModel: AppLockViewModel = koinViewModel()
    val appLockUiState by appLockViewModel.uiState.collectAsStateWithLifecycle()
    val nativeAuthenticator = koinInject<NativeScreenLockAuthenticator>()
    val lifecycleOwner = LocalLifecycleOwner.current
    val systemDark = isSystemInDarkTheme()

    val isDark = when (appUiState.themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    if (onDarkThemeChange != null) {
        LaunchedEffect(isDark) { onDarkThemeChange(isDark) }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> appLockViewModel.onAppForegrounded()
                Lifecycle.Event.ON_STOP -> appLockViewModel.onAppBackgrounded()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    MongsilTheme(
        darkTheme = isDark,
        fontFamily = resolveFontFamily(appUiState.fontStyleOption),
        fontScale = appUiState.fontScale
    ) {
        MainScreen()
        AppLockGate(
            state = appLockUiState,
            nativeAuthenticator = nativeAuthenticator,
            onUnlocked = appLockViewModel::unlock,
        )
    }
}

@Composable
private fun resolveFontFamily(option: FontStyleOption): FontFamily = when (option) {
    FontStyleOption.GAMJA_FLOWER -> FontFamily(Font(resource = Res.font.gamja_flower_regular))
}

internal fun mongsilAppDeclaration(
    additionalDeclaration: KoinApplication.() -> Unit = {},
): KoinAppDeclaration = {
    modules(appModules)
    additionalDeclaration()
}

enum class AppPlatform {
    ANDROID,
    IOS,
    DESKTOP
}

@Composable
expect fun getPlatformName(): String

expect fun getAppPlatform(): AppPlatform
