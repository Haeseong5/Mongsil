package com.cashproject.mongsil.model.db.datasource

import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.dao.CommentDao
import com.cashproject.mongsil.model.db.dao.LockerDao
import io.reactivex.Completable
import io.reactivex.Single

class LocalDataSource(private val commentDao: CommentDao, private val lockerDao: LockerDao) {

    //calendar
    fun getAllComments(): Single<List<Comment>> {
        return commentDao.getAllComments()
    }

    //comment
    fun getCommentsByDocId(docId: String): Single<List<Comment>> {
        return commentDao.getCommentsByDocId(docId)
    }

    fun insertComment(comment: Comment): Completable {
        return commentDao.insert(comment)
    }

    fun deleteCommentById(id: Int): Completable {
        return commentDao.deleteById(id)
    }

    //locker
    fun getAllLikeData(): Single<List<Saying>>{
        return lockerDao.getAll()
    }

    fun findByDocId(docId: String): Single<List<Saying>>{
        return lockerDao.findByDocId(docId)
    }

    fun insertLikeSaying(saying: Saying) : Completable{
        return lockerDao.insert(saying)
    }

    fun deleteLikeSayingByDocId(docId: String) : Completable{
        return lockerDao.delete(docId)
    }

}