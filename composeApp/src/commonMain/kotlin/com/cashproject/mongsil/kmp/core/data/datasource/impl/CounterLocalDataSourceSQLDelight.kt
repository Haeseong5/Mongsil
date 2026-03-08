package com.cashproject.mongsil.kmp.core.data.datasource.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.cashproject.mongsil.kmp.core.data.datasource.CounterLocalDataSource
import com.cashproject.mongsil.kmp.database.MongsilDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CounterLocalDataSourceSQLDelight(private val database: MongsilDatabase) : CounterLocalDataSource {

    override fun getCounterFlow(): Flow<Int> =
        database.counterQueries
            .getCounter()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.count?.toInt() ?: 0 }

    override suspend fun getCounter(): Int =
        withContext(Dispatchers.Default) {
            database.counterQueries.getCounter().executeAsOneOrNull()?.count?.toInt() ?: 0
        }

    override suspend fun saveCounter(value: Int) =
        withContext(Dispatchers.Default) {
            database.counterQueries.insertOrUpdateCounter(count = value.toLong(), lastUpdated = 0L)
        }

    override suspend fun resetCounter() =
        withContext(Dispatchers.Default) {
            database.counterQueries.deleteCounter()
        }
}
