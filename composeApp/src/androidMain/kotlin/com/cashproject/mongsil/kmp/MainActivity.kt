package com.cashproject.mongsil.kmp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.cashproject.mongsil.kmp.screen.setting.screenlock.CurrentActivityHolder
import org.koin.android.ext.android.inject

class MainActivity : FragmentActivity() {
    private val currentActivityHolder: CurrentActivityHolder by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivityHolder.set(this)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
        )

        setContent {
            val view = LocalView.current
            App(
                onDarkThemeChange = { darkTheme ->
                    with(WindowCompat.getInsetsController(window, view)) {
                        isAppearanceLightStatusBars = !darkTheme
                        isAppearanceLightNavigationBars = !darkTheme
                    }
                },
            )
        }
    }

    override fun onResume() {
        super.onResume()
        currentActivityHolder.set(this)
    }

    override fun onDestroy() {
        currentActivityHolder.clear(this)
        super.onDestroy()
    }
}
