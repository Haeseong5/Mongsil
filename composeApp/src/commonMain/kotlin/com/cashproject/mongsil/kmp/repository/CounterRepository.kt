package com.cashproject.mongsil.kmp.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.cashproject.mongsil.kmp.database.MongsilDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Counter 데이터를 관리하는 Repository
 * SQLDelight를 사용하여 로컬 데이터베이스에 저장/조회합니다.
 */
class CounterRepository(private val database: MongsilDatabase) {
    
    /**
     * 저장된 카운터 값을 Flow로 관찰합니다.
     * 데이터베이스가 변경되면 자동으로 업데이트됩니다.
     */
    fun getCounterFlow(): Flow<Int> {
        return database.counterQueries
            .getCounter()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.count?.toInt() ?: 0 }
    }
    
    /**
     * 현재 카운터 값을 조회합니다.
     * 저장된 값이 없으면 0을 반환합니다.
     */
    suspend fun getCounter(): Int = withContext(Dispatchers.Default) {
        database.counterQueries
            .getCounter()
            .executeAsOneOrNull()
            ?.count
            ?.toInt()
            ?: 0
    }
    
    /**
     * 카운터 값을 저장합니다.
     * @param value 저장할 카운터 값
     */
    suspend fun saveCounter(value: Int) = withContext(Dispatchers.Default) {
        database.counterQueries.insertOrUpdateCounter(
            count = value.toLong(),
            lastUpdated = 0L // 간단하게 처리, 필요시 kotlinx-datetime 사용 가능
        )
    }
    
    /**
     * 카운터를 초기화합니다 (값 삭제).
     */
    suspend fun resetCounter() = withContext(Dispatchers.Default) {
        database.counterQueries.deleteCounter()
    }
}
