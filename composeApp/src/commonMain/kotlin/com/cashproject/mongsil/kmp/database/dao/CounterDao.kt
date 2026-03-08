package com.cashproject.mongsil.kmp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cashproject.mongsil.kmp.database.entity.CounterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {

    @Query("SELECT * FROM CounterEntity WHERE id = 1")
    suspend fun getCounter(): CounterEntity?

    @Query("SELECT * FROM CounterEntity WHERE id = 1")
    fun getCounterFlow(): Flow<CounterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(counter: CounterEntity)

    @Query("DELETE FROM CounterEntity WHERE id = 1")
    suspend fun delete()
}
