package com.cashproject.mongsil.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cashproject.mongsil.kmp.repository.CounterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Counter 기능을 담당하는 ViewModel
 * SQLDelight를 통해 데이터를 영구 저장합니다.
 */
class CounterViewModel(
    private val repository: CounterRepository
) {
    // ViewModel의 생명주기를 관리하는 CoroutineScope
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    // 현재 카운터 값을 Compose State로 관리
    var count by mutableStateOf(0)
        private set
    
    // 로딩 상태
    var isLoading by mutableStateOf(true)
        private set
    
    init {
        // 앱 시작 시 저장된 카운터 값을 로드
        loadCounter()
    }
    
    /**
     * 데이터베이스에서 카운터 값을 로드합니다.
     */
    private fun loadCounter() {
        viewModelScope.launch {
            isLoading = true
            count = repository.getCounter()
            isLoading = false
        }
    }
    
    /**
     * 카운터 값을 1 증가시키고 데이터베이스에 저장합니다.
     */
    fun increment() {
        count++
        saveCounter()
    }
    
    /**
     * 카운터 값을 1 감소시키고 데이터베이스에 저장합니다.
     */
    fun decrement() {
        if (count > 0) {
            count--
            saveCounter()
        }
    }
    
    /**
     * 카운터를 0으로 초기화하고 데이터베이스에서 삭제합니다.
     */
    fun reset() {
        viewModelScope.launch {
            repository.resetCounter()
            count = 0
        }
    }
    
    /**
     * 현재 카운터 값을 데이터베이스에 저장합니다.
     */
    private fun saveCounter() {
        viewModelScope.launch {
            repository.saveCounter(count)
        }
    }
    
    /**
     * ViewModel이 더 이상 사용되지 않을 때 호출됩니다.
     * 코루틴을 정리합니다.
     */
    fun onCleared() {
        viewModelScope.cancel()
    }
}
