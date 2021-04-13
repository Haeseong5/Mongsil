package com.cashproject.mongsil.model.db.dao

import androidx.room.*
import com.cashproject.mongsil.model.data.Saying
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

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