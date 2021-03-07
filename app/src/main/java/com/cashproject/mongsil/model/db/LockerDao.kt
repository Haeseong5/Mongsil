package com.cashproject.mongsil.model.db

import androidx.room.*
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
import com.google.firebase.firestore.auth.User
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface LockerDao {
    @Query("SELECT * FROM LikeSaying")
    fun getAll(): Single<List<LikeSaying>>

    @Query("SELECT * FROM LikeSaying WHERE docId = :docId")
    fun findByDocId(docId: String): Single<List<LikeSaying>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(saying: LikeSaying) : Completable

    @Query("DELETE FROM LikeSaying WHERE docId = :docId")
    fun delete(docId: String) : Completable

}