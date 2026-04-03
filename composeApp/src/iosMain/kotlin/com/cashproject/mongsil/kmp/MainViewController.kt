package com.cashproject.mongsil.kmp

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.compose.KoinApplication
import platform.UIKit.UIViewController

fun MainViewController(onDarkThemeChange: (Boolean) -> Unit = {}): UIViewController {
    Napier.base(DebugAntilog())
    return ComposeUIViewController {
        KoinApplication(
            application = mongsilAppDeclaration()
        ) {
            App(onDarkThemeChange = onDarkThemeChange)
        }
    }
}
