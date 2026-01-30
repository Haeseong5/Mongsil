package com.cashproject.mongsil.kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.cashproject.mongsil.kmp.di.platformModule
import org.koin.compose.KoinApplication

fun MainViewController() = ComposeUIViewController {
    KoinApplication(
        application = mongsilAppDeclaration {
            modules(platformModule())
        }
    ) {
        App()
    }
}
