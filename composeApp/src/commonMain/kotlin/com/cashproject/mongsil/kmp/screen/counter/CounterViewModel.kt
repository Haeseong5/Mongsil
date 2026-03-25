package com.cashproject.mongsil.kmp.screen.counter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.CounterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Counter 기능을 담당하는 ViewModel
 * SQLDelight를 통해 데이터를 영구 저장합니다.
 */
class CounterViewModel(
    private val repository: CounterRepository
) : BaseViewModel() {
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
        viewModelScope.launch(exceptionHandler) {
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
        viewModelScope.launch(exceptionHandler) {
            repository.resetCounter()
            count = 0
        }
    }

    /**
     * 현재 카운터 값을 데이터베이스에 저장합니다.
     */
    private fun saveCounter() {
        viewModelScope.launch(exceptionHandler) {
            repository.saveCounter(count)
        }
    }
}