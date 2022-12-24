package com.cashproject.mongsil.data.db.dao

import androidx.room.*
import com.cashproject.mongsil.data.db.entity.Saying
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface LockerDao {

    @Query("SELECT * FROM Saying")
    suspend fun getAll(): List<Saying>

    @Query("SELECT * FROM Saying WHERE docId = :docId")
    fun findByDocId(docId: String): Maybe<Saying>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(saying: Saying) : Completable

    @Query("DELETE FROM Saying WHERE docId = :docId")
    fun delete(docId: String) : Completable

}