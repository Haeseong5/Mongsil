package com.cashproject.mongsil.kmp.core.data.datasource.impl

import com.cashproject.mongsil.kmp.core.data.datasource.CounterLocalDataSource
import com.cashproject.mongsil.kmp.database.dao.CounterDao
import com.cashproject.mongsil.kmp.database.entity.CounterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CounterLocalDataSourceRoom(private val counterDao: CounterDao) : CounterLocalDataSource {

    override fun getCounterFlow(): Flow<Int> =
        counterDao.getCounterFlow().map { it?.count?.toInt() ?: 0 }

    override suspend fun getCounter(): Int =
        counterDao.getCounter()?.count?.toInt() ?: 0

    override suspend fun saveCounter(value: Int) {
        counterDao.insertOrUpdate(CounterEntity(count = value.toLong(), lastUpdated = 0L))
    }

    override suspend fun resetCounter() {
        counterDao.delete()
    }
}
