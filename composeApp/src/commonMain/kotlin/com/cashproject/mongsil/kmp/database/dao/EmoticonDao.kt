package com.cashproject.mongsil.kmp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cashproject.mongsil.kmp.database.entity.EmoticonEntity

@Dao
interface EmoticonDao {

    @Query("SELECT * FROM EmoticonEntity ORDER BY id ASC")
    suspend fun getAllEmoticons(): List<EmoticonEntity>

    @Query("SELECT * FROM EmoticonEntity WHERE id = :id")
    suspend fun getEmoticonById(id: Long): EmoticonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(emoticon: EmoticonEntity)

    @Query("DELETE FROM EmoticonEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM EmoticonEntity")
    suspend fun getCount(): Long

    @Query("SELECT MAX(lastUpdated) FROM EmoticonEntity")
    suspend fun getLastUpdatedTime(): Long?
}
