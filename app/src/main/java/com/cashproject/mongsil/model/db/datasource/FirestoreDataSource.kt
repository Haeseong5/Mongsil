package com.cashproject.mongsil.model.db.datasource

import com.cashproject.mongsil.base.ApplicationClass
import com.cashproject.mongsil.util.DateUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class FirestoreDataSource {

    companion object {
        const val COLLECTION = "Mongsil"
    }

    val db by lazy {
        FirebaseFirestore.getInstance()
    }

    /*Home*/
    fun getLatestData(): Task<QuerySnapshot> {
        return db.collection(COLLECTION)
            .whereLessThan(
                ApplicationClass.DATE,
                DateUtil.dateToTimestamp(Date())
            )  //오늘보다 이전 날 중 가장 최신 데이터 가져오기
            .orderBy(ApplicationClass.DATE, Query.Direction.DESCENDING) //최신 날짜순으로 조
            .limit(1)
            .get()
    }

    fun getTodayData(): Task<QuerySnapshot> {
        return db.collection(COLLECTION)
            .whereEqualTo(
                ApplicationClass.DATE,
                DateUtil.dateToTimestamp(Date())
            )            //오늘 날짜 명언 가져오기
            .limit(1)
            .get()
    }

    fun getSingleSayingData(docId: String): Task<DocumentSnapshot> {
        return db.collection(COLLECTION).document(docId)
            .get()
    }


    /*
        Calendar
     */
    fun getSayingList(): Task<QuerySnapshot> {
        return db.collection(COLLECTION)
            .get()
    }


    fun getDataByDate(date: Date): Task<QuerySnapshot> {
        return db.collection(COLLECTION)
            .whereEqualTo(ApplicationClass.DATE, DateUtil.dateToTimestamp(date))
            .limit(1)
            .get()
    }
}