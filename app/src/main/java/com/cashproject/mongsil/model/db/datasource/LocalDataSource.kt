package com.cashproject.mongsil.model.db.datasource

import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.db.dao.CommentDao
import com.cashproject.mongsil.model.db.dao.LockerDao
import io.reactivex.Completable
import io.reactivex.Single

class LocalDataSource(private val commentDao: CommentDao, private val lockerDao: LockerDao) {

    //comment
    fun getComments(docId: String): Single<List<Comment>> {
        return commentDao.getComments(docId)
    }

    fun insertComment(comment: Comment): Completable {
        return commentDao.insert(comment)
    }

    fun deleteCommentById(id: Int): Completable {
        return commentDao.deleteById(id)
    }

    //locker
    fun getAllLikeData(): Single<List<LikeSaying>>{
        return lockerDao.getAll()
    }

    fun findByDocId(docId: String): Single<List<LikeSaying>>{
        return lockerDao.findByDocId(docId)
    }

    fun insertLikeSaying(saying: LikeSaying) : Completable{
        return lockerDao.insert(saying)
    }

    fun deleteLikeSayingByDocId(docId: String) : Completable{
        return lockerDao.delete(docId)
    }

}