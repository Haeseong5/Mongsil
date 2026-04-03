package com.cashproject.mongsil.kmp.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _errorEvent = MutableSharedFlow<Unit>()
    val errorEvent = _errorEvent.asSharedFlow()

    protected val exceptionHandler = CoroutineExceptionHandler { _, error ->
        Napier.e(tag = this::class.simpleName ?: "BaseViewModel", throwable = error) {
            error.message ?: "Unknown error"
        }
        viewModelScope.launch {
            _errorEvent.emit(Unit)
        }
    }
}
