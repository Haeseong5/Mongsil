package com.cashproject.mongsil.kmp

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.AppUpdateDialog
import com.cashproject.mongsil.kmp.di.appModules
import com.cashproject.mongsil.kmp.migration.MigrationLoadingScreen
import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ThemeMode
import com.cashproject.mongsil.kmp.screen.main.MainScreen
import com.cashproject.mongsil.kmp.screen.setting.screenlock.AppLockGate
import com.cashproject.mongsil.kmp.screen.setting.screenlock.AppLockViewModel
import com.cashproject.mongsil.kmp.screen.setting.screenlock.NativeScreenLockAuthenticator
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.gamja_flower_regular
import mongsil.composeapp.generated.resources.ic_app_logo
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
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
        when (appUiState.migrationState) {
            MigrationState.IDLE -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_app_logo),
                        contentDescription = null,
                        modifier = Modifier.clip(CircleShape).size(160.dp),
                    )
                }
            }

            MigrationState.MIGRATING -> {
                MigrationLoadingScreen()
            }

            MigrationState.DONE -> {
                MainScreen()
                AppLockGate(
                    state = appLockUiState,
                    nativeAuthenticator = nativeAuthenticator,
                    onUnlocked = appLockViewModel::unlock,
                )
                appUiState.updateInfo?.let { updateInfo ->
                    AppUpdateDialog(
                        currentVersion = updateInfo.currentVersion,
                        latestVersion = updateInfo.latestVersion,
                        onUpdate = {
                            appViewModel.dismissUpdateDialog()
                            openAppStore()
                        },
                        onDismiss = appViewModel::dismissUpdateDialog,
                    )
                }
            }
        }
    }
}

// 폰트 종류 추가는 여기에서
@Composable
private fun resolveFontFamily(option: FontStyleOption): FontFamily = when (option) {
    FontStyleOption.SYSTEM -> FontFamily.Default
    FontStyleOption.GAMJA_FLOWER -> FontFamily(Font(resource = Res.font.gamja_flower_regular))
}

internal fun mongsilAppDeclaration(
    additionalDeclaration: KoinApplication.() -> Unit = {},
): KoinAppDeclaration = {
    modules(appModules)
    additionalDeclaration()
}
