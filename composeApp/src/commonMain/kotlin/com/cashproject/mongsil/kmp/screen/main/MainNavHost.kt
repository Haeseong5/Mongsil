package com.cashproject.mongsil.kmp.screen.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cashproject.mongsil.kmp.AppPlatform
import com.cashproject.mongsil.kmp.designsystem.component.rememberSnackbarController
import com.cashproject.mongsil.kmp.getAppPlatform
import com.cashproject.mongsil.kmp.screen.calendar.CalendarScreen
import com.cashproject.mongsil.kmp.screen.counter.CounterScreen
import com.cashproject.mongsil.kmp.screen.diarychart.DiaryChartScreen
import com.cashproject.mongsil.kmp.screen.diarychart.DiaryChartViewModel
import com.cashproject.mongsil.kmp.screen.diarymonthly.DiaryListScreen
import com.cashproject.mongsil.kmp.screen.diarymonthly.DiaryListViewModel
import com.cashproject.mongsil.kmp.screen.diarysearch.DiarySearchScreen
import com.cashproject.mongsil.kmp.screen.diarywrite.DiaryWriteScreen
import com.cashproject.mongsil.kmp.screen.diarywrite.DiaryWriteViewModel
import com.cashproject.mongsil.kmp.screen.setting.SettingScreen
import com.cashproject.mongsil.kmp.screen.setting.appreview.AppReviewScreen
import com.cashproject.mongsil.kmp.screen.setting.backup.BackupRestoreScreen
import com.cashproject.mongsil.kmp.screen.setting.fontstyle.FontStyleScreen
import com.cashproject.mongsil.kmp.screen.setting.language.LanguageSettingScreen
import com.cashproject.mongsil.kmp.screen.setting.pdfexport.PdfExportScreen
import com.cashproject.mongsil.kmp.screen.setting.screenlock.ScreenLockScreen
import com.cashproject.mongsil.kmp.screen.setting.store.MongsilStoreScreen
import com.cashproject.mongsil.kmp.screen.setting.theme.ThemeSettingScreen
import com.cashproject.mongsil.kmp.screen.test.TestScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


private const val NAV_ANIM_DURATION = 500
private const val ANDROID_PLAY_STORE_URL =
    "https://play.google.com/store/apps/details?id=com.cashproject.mongsil"

@Composable
internal fun MainNavHost(
    navigator: NavHostController,
    padding: PaddingValues,
    startDestination: Route = Route.Calendar
) {
    val uriHandler = LocalUriHandler.current
    val snackbarController = rememberSnackbarController()
    val platform = getAppPlatform()

    NavHost(
        navController = navigator,
        startDestination = startDestination,
        enterTransition = { fadeIn(tween(NAV_ANIM_DURATION)) }, // 앞으로 이동 시, 들어오는 화면
        exitTransition = { fadeOut(tween(NAV_ANIM_DURATION)) }, //새 화면으로 이동할 때 현재 화면이 사라지는 애니메이션
        popEnterTransition = { fadeIn(tween(NAV_ANIM_DURATION)) }, //뒤로가기 시, 들어오는 화면
        popExitTransition = { fadeOut(tween(NAV_ANIM_DURATION)) } //뒤로가기 시, 현재 화면이 사라지는 애니메이션
    ) {
        composable<Route.Calendar> {
            CalendarScreen(
                padding = padding,
                onNavigateToDiaryWrite = { year, month, day ->
                    navigator.navigateTo(Route.DiaryWrite(year, month, day))
                },
                onNavigateToSetting = {
                    navigator.navigateTo(Route.Setting)
                },
                onNavigateToSearch = {
                    navigator.navigateTo(Route.DiarySearch)
                },
                onNavigateToChart = { year, month ->
                    navigator.navigateTo(Route.DiaryChart(year, month))
                },
                onNavigateToMonthly = { year, month ->
                    navigator.navigateTo(Route.DiaryMonthly(year, month))
                }
            )
        }

        composable<Route.DiaryWrite> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.DiaryWrite>()
            val viewModel: DiaryWriteViewModel = koinViewModel {
                parametersOf(route.year, route.month, route.day)
            }

            DiaryWriteScreen(
                padding = padding,
                viewModel = viewModel,
                onBack = {
                    navigator.popBackStack()
                }
            )
        }

        composable<Route.Counter> {
            CounterScreen(
                onNavigateToCalendar = {
                    navigator.navigateAndClearStack(Route.Calendar)
                }
            )
        }

        composable<Route.Test> {
            TestScreen(
                onClick = {
                    navigator.navigateAndClearStack(Route.Calendar)
                }
            )
        }

        composable<Route.Setting> {
            SettingScreen(
                onBack = { navigator.popBackStack() },
                onNavigateToMongsilStore = { navigator.navigateTo(Route.MongsilStore) },
                onNavigateToThemeSetting = { navigator.navigateTo(Route.ThemeSetting) },
                onNavigateToFontStyle = { navigator.navigateTo(Route.FontStyle) },
                onNavigateToScreenLock = { navigator.navigateTo(Route.ScreenLock) },
                onNavigateToBackupRestore = { navigator.navigateTo(Route.BackupRestore) },
                onNavigateToPdfExport = { navigator.navigateTo(Route.PdfExport) },
                onNavigateToLanguageSetting = { navigator.navigateTo(Route.LanguageSetting) },
                onNavigateToAppReview = {
                    when (platform) {
                        AppPlatform.ANDROID -> uriHandler.openUri(ANDROID_PLAY_STORE_URL)
                        AppPlatform.IOS, AppPlatform.DESKTOP -> {
                            snackbarController.showSnackbar("준비중입니다")
                        }
                    }
                }
            )
        }

        composable<Route.MongsilStore> {
            MongsilStoreScreen(
                onBack = { navigator.popBackStack() }
            )
        }

        composable<Route.ThemeSetting> {
            ThemeSettingScreen(
                padding = padding,
                onBack = { navigator.popBackStack() }
            )
        }

        composable<Route.FontStyle> {
            FontStyleScreen(
                padding = padding,
                onBack = { navigator.popBackStack() }
            )
        }

        composable<Route.ScreenLock> {
            ScreenLockScreen(
                onBack = { navigator.popBackStack() }
            )
        }

        composable<Route.BackupRestore> {
            BackupRestoreScreen(
                onBack = { navigator.popBackStack() }
            )
        }

        composable<Route.PdfExport> {
            PdfExportScreen(
                padding = padding,
                onBack = { navigator.popBackStack() }
            )
        }

        composable<Route.LanguageSetting> {
            LanguageSettingScreen(
                onBack = { navigator.popBackStack() }
            )
        }

        composable<Route.AppReview> {
            AppReviewScreen(
                onBack = { navigator.popBackStack() }
            )
        }

        composable<Route.DiarySearch> {
            DiarySearchScreen(
                padding = padding,
                onBack = { navigator.popBackStack() },
                onDiaryClick = { year, month, day ->
                    navigator.navigateTo(Route.DiaryWrite(year, month, day))
                }
            )
        }

        composable<Route.DiaryChart> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.DiaryChart>()
            val viewModel: DiaryChartViewModel = koinViewModel {
                parametersOf(route.year, route.month)
            }

            DiaryChartScreen(
                viewModel = viewModel,
                padding = padding,
                onClose = { navigator.popBackStack() }
            )
        }

        composable<Route.DiaryMonthly> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.DiaryMonthly>()
            val viewModel: DiaryListViewModel = koinViewModel {
                parametersOf(route.year, route.month)
            }

            DiaryListScreen(
                viewModel = viewModel,
                padding = padding,
                onBack = { navigator.popBackStack() },
                onDiaryClick = { year, month, day ->
                    navigator.navigateTo(Route.DiaryWrite(year, month, day))
                }
            )
        }
    }
}

/**
 * 백스택을 초기화하고 명시한 화면으로 이동
 *
 * @param route 이동할 route (파라미터가 있는 경우 data class 인스턴스 전달)
 * @param clearRoute 백스택에서 제거할 기준 route (기본값: 현재 startDestination)
 * @param builder 추가 NavOptions 설정 (optional)
 *
 * 사용 예시:
 * ```
 * navigator.navigateAndClearStack(Route.Calendar)
 * navigator.navigateAndClearStack(Route.DiaryDetail(diaryId = 123, date = "2024-01-30"))
 * ```
 */
fun NavHostController.navigateAndClearStack(
    route: Route,
    clearRoute: Route? = null,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    navigate(route) {
        // 백스택 초기화
        val targetRoute = clearRoute ?: graph.startDestinationRoute
        if (targetRoute != null) {
            popUpTo(graph.findStartDestination().id) {
                inclusive = true
                saveState = false
            }
        } else {
            // startDestination이 없는 경우 그래프 전체 제거
            popUpTo(0) {
                inclusive = true
            }
        }

        // 중복 방지
        launchSingleTop = true

        // 추가 옵션 적용
        builder?.invoke(this)
    }
}

/**
 * 백스택을 쌓고 화면으로 이동
 *
 * @param route 이동할 route (파라미터가 있는 경우 data class 인스턴스 전달)
 * @param singleTop 동일한 화면이 이미 최상단에 있을 경우 재사용 여부 (기본값: true)
 * @param restoreState 이전 상태 복원 여부 (기본값: false)
 * @param builder 추가 NavOptions 설정 (optional)
 *
 * 사용 예시:
 * ```
 * navigator.navigateTo(Route.Counter)
 * navigator.navigateTo(Route.DiaryDetail(diaryId = 123, date = "2024-01-30"))
 * navigator.navigateTo(Route.Settings, singleTop = false)
 * ```
 */
fun NavHostController.navigateTo(
    route: Route,
    singleTop: Boolean = true,
    restoreState: Boolean = false,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    navigate(route) {
        // 중복 방지 옵션
        launchSingleTop = singleTop

        // 상태 복원
        this.restoreState = restoreState

        // 추가 옵션 적용
        builder?.invoke(this)
    }
}

/**
 * 백스택의 특정 화면까지 popUpTo하면서 이동
 *
 * @param route 이동할 route
 * @param popUpTo 백스택에서 제거할 기준 route
 * @param inclusive popUpTo route도 함께 제거할지 여부 (기본값: false)
 * @param singleTop 중복 방지 여부 (기본값: true)
 *
 * 사용 예시:
 * ```
 * // Calendar까지 백스택 제거하고 (Calendar는 유지) Counter로 이동
 * navigator.navigateWithPopUpTo(Route.Counter, popUpTo = Route.Calendar)
 *
 * // Calendar까지 백스택 제거하고 (Calendar도 제거) Home으로 이동
 * navigator.navigateWithPopUpTo(Route.Home, popUpTo = Route.Calendar, inclusive = true)
 * ```
 */
fun NavHostController.navigateWithPopUpTo(
    route: Route,
    popUpTo: Route,
    inclusive: Boolean = false,
    singleTop: Boolean = true
) {
    navigate(route) {
        popUpTo(popUpTo) {
            this.inclusive = inclusive
        }
        launchSingleTop = singleTop
    }
}
