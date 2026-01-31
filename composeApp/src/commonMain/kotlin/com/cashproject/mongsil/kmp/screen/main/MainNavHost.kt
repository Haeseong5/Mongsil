package com.cashproject.mongsil.kmp.screen.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cashproject.mongsil.kmp.screen.calendar.CalendarScreen
import com.cashproject.mongsil.kmp.screen.counter.CounterScreen
import com.cashproject.mongsil.kmp.screen.test.TestScreen


@Composable
internal fun MainNavHost(
    navigator: NavHostController,
    startDestination: Route = Route.Calendar
) {
    NavHost(
        navController = navigator,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<Route.Calendar> {
            CalendarScreen()
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