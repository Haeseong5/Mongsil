package com.cashproject.mongsil.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cashproject.mongsil.kmp.di.getKoinModules
import com.cashproject.mongsil.kmp.screen.calendar.CalendarScreen
import kotlinx.datetime.LocalDate
import org.koin.compose.KoinApplication

/**
 * Navigation Routes
 */
object Routes {
    const val HOME = "home"
    const val CALENDAR = "calendar"
}

/**
 * Android 전용 Navigation App
 * androidx.navigation.compose 사용
 */
@Composable
fun NavigationApp(koinConfiguration: (org.koin.core.KoinApplication.() -> Unit)? = null) {
    // Koin 초기화
    KoinApplication(application = {
        modules(getKoinModules())
        koinConfiguration?.invoke(this)
    }) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // NavController 생성
                val navController = rememberNavController()
                
                // 샘플 데이터: 일기가 작성된 날짜들
                val recordedDates = remember { 
                    setOf(
                        LocalDate(2026, 1, 1),
                        LocalDate(2026, 1, 5),
                        LocalDate(2026, 1, 6),
                    )
                }
                
                // Navigation 설정
                NavHost(
                    navController = navController,
                    startDestination = Routes.HOME
                ) {
                    // 홈(카운터) 화면
                    composable(Routes.HOME) {
                        CounterScreen(
                            onNavigateToCalendar = {
                                navController.navigate(Routes.CALENDAR)
                            }
                        )
                    }
                    
                    // 캘린더 화면
                    composable(Routes.CALENDAR) {
                        CalendarScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            recordedDates = recordedDates,
                            onDateClick = { date ->
                                println("날짜 클릭: $date")
                                // TODO: 일기 작성 화면으로 이동
                            }
                        )
                    }
                }
            }
        }
    }
}
