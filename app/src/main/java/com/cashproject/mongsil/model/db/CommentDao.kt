package com.cashproject.mongsil.model.db

import androidx.room.*
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
import com.google.firebase.firestore.auth.User
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CommentDao {

    @Query("SELECT * FROM Comment WHERE docId = :docId")
    fun getComments(docId: String): Single<List<Comment>>

    @Insert
    fun insert(comment: Comment) : Completable

    @Query("DELETE FROM Comment WHERE id = :id")
    fun deleteById(id: Int) : Completable

}