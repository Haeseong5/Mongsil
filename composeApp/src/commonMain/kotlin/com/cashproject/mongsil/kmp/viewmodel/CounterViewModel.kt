package com.cashproject.mongsil.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Counter 기능을 담당하는 ViewModel
 * Koin을 통해 주입되어 사용됩니다.
 */
class CounterViewModel {
    // 현재 카운터 값을 Compose State로 관리
    var count by mutableStateOf(0)
        private set
    
    /**
     * 카운터 값을 1 증가시킵니다.
     */
    fun increment() {
        count++
    }
    
    /**
     * 카운터 값을 1 감소시킵니다.
     */
    fun decrement() {
        if (count > 0) {
            count--
        }
    }
    
    /**
     * 카운터를 0으로 초기화합니다.
     */
    fun reset() {
        count = 0
    }
}
