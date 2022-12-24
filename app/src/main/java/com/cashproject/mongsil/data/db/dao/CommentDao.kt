package com.cashproject.mongsil.data.db.dao

import androidx.room.*
import com.cashproject.mongsil.data.db.entity.CommentEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CommentDao {

    @Query("SELECT * FROM Comment WHERE docId = :docId ORDER BY time ASC")
    fun getCommentsByDocId(docId: String): Single<List<CommentEntity>>

    @Query("SELECT * FROM Comment")
    fun getAllComments() : Single<List<CommentEntity>>

    @Insert
    fun insert(commentEntity: CommentEntity) : Completable

    @Query("DELETE FROM Comment WHERE id = :id")
    fun deleteById(id: Int) : Completable

}