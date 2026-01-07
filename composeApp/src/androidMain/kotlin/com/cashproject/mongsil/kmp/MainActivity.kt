package com.cashproject.mongsil.kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Android 전용 Navigation 사용
            NavigationApp { 
                // Android Context를 Koin에 제공
                androidContext(this@MainActivity.applicationContext)
            }
        }
    }
}
