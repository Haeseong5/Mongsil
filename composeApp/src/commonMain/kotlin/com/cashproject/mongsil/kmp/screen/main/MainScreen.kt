package com.cashproject.mongsil.kmp.screen.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cashproject.mongsil.kmp.Route
import com.cashproject.mongsil.kmp.model.MongsilMood
import com.cashproject.mongsil.kmp.screen.calendar.CalendarScreen
import com.cashproject.mongsil.kmp.screen.counter.CounterScreen
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun MainScreen() {
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
                // 샘플 감정 데이터 생성
                val today = remember {
                    Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                }

                val sampleMoods = remember {
                    // 현재 월의 샘플 감정 데이터
                    mapOf(
                        LocalDate(today.year, today.monthNumber, 1) to MongsilMood.VERY_HAPPY,
                        LocalDate(today.year, today.monthNumber, 2) to MongsilMood.HAPPY,
                        LocalDate(today.year, today.monthNumber, 3) to MongsilMood.EXCITED,
                        LocalDate(today.year, today.monthNumber, 6) to MongsilMood.LOVELY,
                        LocalDate(today.year, today.monthNumber, 9) to MongsilMood.PEACEFUL,
                        LocalDate(today.year, today.monthNumber, 15) to MongsilMood.NORMAL,
                        LocalDate(today.year, today.monthNumber, 20) to MongsilMood.TIRED,
                        LocalDate(today.year, today.monthNumber, 25) to MongsilMood.SAD
                    )
                }

                CalendarScreen(
                    moods = sampleMoods,
                    recordedDates = sampleMoods.keys,
                    onDateClick = { date ->
                        println("날짜 클릭: $date")
                    },
                    onAddClick = {
                        println("새 일기 작성하기")
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