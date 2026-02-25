package com.cashproject.mongsil.kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.cashproject.mongsil.kmp.di.platformModule
import org.koin.compose.KoinApplication
import platform.UIKit.UIViewController

fun MainViewController(onDarkThemeChange: (Boolean) -> Unit = {}): UIViewController =
    ComposeUIViewController {
        KoinApplication(
            application = mongsilAppDeclaration {
                modules(platformModule())
            }
        ) {
            App(onDarkThemeChange = onDarkThemeChange)
        }
    }
