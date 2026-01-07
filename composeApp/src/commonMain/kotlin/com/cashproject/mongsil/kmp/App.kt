package com.cashproject.mongsil.kmp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.kmp.di.getKoinModules
import com.cashproject.mongsil.kmp.screen.calendar.CalendarScreen
import com.cashproject.mongsil.kmp.viewmodel.CounterViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

enum class Screen {
    COUNTER,
    CALENDAR
}

@Composable
fun App(koinConfiguration: (org.koin.core.KoinApplication.() -> Unit)? = null) {
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
                var currentScreen by remember { mutableStateOf(Screen.COUNTER) }
                
                // 샘플 데이터: 일기가 작성된 날짜들
                val recordedDates = remember { 
                    setOf(
                        kotlinx.datetime.LocalDate(2026, 1, 1),
                        kotlinx.datetime.LocalDate(2026, 1, 5),
                        kotlinx.datetime.LocalDate(2026, 1, 6),
                    )
                }
                
                when (currentScreen) {
                    Screen.COUNTER -> CounterScreen(
                        onNavigateToCalendar = { currentScreen = Screen.CALENDAR }
                    )
                    Screen.CALENDAR -> CalendarScreen(
                        onNavigateBack = { currentScreen = Screen.COUNTER },
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

@Composable
fun CounterScreen(
    viewModel: CounterViewModel = koinInject(),
    onNavigateToCalendar: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            // 타이틀
            Text(
                text = "🎉 몽실 Counter",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Koin + ViewModel 예제",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 카운터 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "현재 값",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 카운터 값 표시
                    Text(
                        text = "${viewModel.count}",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 버튼들
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 감소 버튼
                OutlinedButton(
                    onClick = { viewModel.decrement() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "➖",
                        fontSize = 24.sp
                    )
                }

                // 증가 버튼
                Button(
                    onClick = { viewModel.increment() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "➕",
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 초기화 버튼
            OutlinedButton(
                onClick = { viewModel.reset() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🔄 초기화")
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // 캘린더 이동 버튼
            FilledTonalButton(
                onClick = onNavigateToCalendar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📅 캘린더 보기")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 플랫폼 정보
            Text(
                text = getPlatformName(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
expect fun getPlatformName(): String
