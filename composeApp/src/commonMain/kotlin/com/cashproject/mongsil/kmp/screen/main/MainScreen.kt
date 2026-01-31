package com.cashproject.mongsil.kmp.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme

//@Composable
//expect fun MainScreen(modifier: Modifier = Modifier)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navigator: NavHostController = rememberNavController()
) {
    MongsilTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MongsilTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MainNavHost(navigator)
            }
        }
    }
}