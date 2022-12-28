package com.cashproject.mongsil.data.db.dao

import androidx.room.*
import com.cashproject.mongsil.data.db.entity.SayingEntity
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM Saying")
    suspend fun getAll(): List<SayingEntity>

    @Query("SELECT * FROM Saying WHERE docId = :docId")
    suspend fun findByDocId(docId: String): SayingEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sayingEntity: SayingEntity)

    @Query("DELETE FROM Saying WHERE docId = :docId")
    suspend fun delete(docId: String)

}