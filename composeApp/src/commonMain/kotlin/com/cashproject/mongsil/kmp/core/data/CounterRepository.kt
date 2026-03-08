package com.cashproject.mongsil.kmp.core.data

import com.cashproject.mongsil.kmp.core.data.datasource.CounterLocalDataSource
import kotlinx.coroutines.flow.Flow

class CounterRepository(private val localDataSource: CounterLocalDataSource) {

    fun getCounterFlow(): Flow<Int> = localDataSource.getCounterFlow()

    suspend fun getCounter(): Int = localDataSource.getCounter()

    suspend fun saveCounter(value: Int) = localDataSource.saveCounter(value)

    suspend fun resetCounter() = localDataSource.resetCounter()
}
