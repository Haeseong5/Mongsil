package com.cashproject.mongsil.kmp.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.LocalSnackbarController
import com.cashproject.mongsil.kmp.designsystem.component.SnackbarController

//@Composable
//expect fun MainScreen(modifier: Modifier = Modifier)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navigator: NavHostController = rememberNavController()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarController = remember(snackbarHostState, coroutineScope) {
        SnackbarController(snackbarHostState, coroutineScope)
    }

    MongsilTheme {
        CompositionLocalProvider(LocalSnackbarController provides snackbarController) {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                containerColor = MongsilTheme.colorScheme.background,
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { paddingValues ->
                Box(modifier = Modifier.fillMaxSize()) {
                    MainNavHost(
                        navigator = navigator,
                        padding = paddingValues
                    )
                }
            }
        }
    }
}