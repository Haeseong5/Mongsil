package com.cashproject.mongsil.kmp.core.data.datasource

import kotlinx.coroutines.flow.Flow

interface CounterLocalDataSource {
    fun getCounterFlow(): Flow<Int>
    suspend fun getCounter(): Int
    suspend fun saveCounter(value: Int)
    suspend fun resetCounter()
}
