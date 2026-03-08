package com.cashproject.mongsil.kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

fun main() {
    startKoin(mongsilAppDeclaration())

    application {
        Window(
            onCloseRequest = {
                stopKoin()
                exitApplication()
            },
            title = "몽실",
        ) {
            App()
        }
    }
}
