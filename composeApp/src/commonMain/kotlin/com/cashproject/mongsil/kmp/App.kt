package com.cashproject.mongsil.kmp

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cashproject.mongsil.kmp.screen.calendar.CalendarScreen
import com.cashproject.mongsil.kmp.screen.counter.CounterScreen
import org.koin.core.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


@Composable
fun App() {
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = Route.Calendar,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable<Route.Calendar> {
                CalendarScreen(
                    onDateClick = { date ->
                        println("날짜 클릭: $date")
                    }
                )
            }

            composable<Route.Counter> {
                CounterScreen(
                    onNavigateToCalendar = {
                        // Type-safe navigation: 객체를 직접 전달
                        navController.navigate(Route.Home)
                    }
                )
            }

            // 파라미터가 있는 경우 예시:
            // composable<Route.DiaryDetail> { backStackEntry ->
            //     val args = backStackEntry.toRoute<Route.DiaryDetail>()
            //     DiaryDetailScreen(
            //         diaryId = args.diaryId,
            //         date = args.date
            //     )
            // }
        }
    }
}

internal val appModule = module {
//    // Firebase expect 클래스들을 등록
//    single { FirebaseFirestore() }
//    single { FirebaseAuth() }
//
//    // Repository들 등록
//    single<CheckInRepository> { CheckInRepositoryImpl(get(), get()) }
//    single<AuthRepository> { AuthRepositoryImpl(get()) }
//
//    // ViewModel들 등록
//    viewModelOf(::CheckInViewModel)
//    viewModelOf(::HistoryViewModel)
//    viewModelOf(::AppViewModel)
//    viewModelOf(::AuthViewModel)
}

internal fun mongsilAppDeclaration(
    additionalDeclaration: KoinApplication.() -> Unit = {},
): KoinAppDeclaration = {
    modules(appModule)
    additionalDeclaration()
}


@Composable
expect fun getPlatformName(): String
