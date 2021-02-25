package com.cashproject.mongsil.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.firebase.FirebaseManager
import com.cashproject.mongsil.model.data.Saying
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirebaseViewModel: BaseViewModel(){

    private val COLLECTION = "Mongsil"
    private val DATE = "date"

    private val _sayingData = MutableLiveData<List<Saying>>()
    val sayingData: LiveData<List<Saying>>
        get() = _sayingData

    val storage by lazy {
        FirebaseStorage.getInstance()
    }
    val db by lazy {
        Firebase.firestore
    }

    val storageRef = storage.reference

    fun getData(){

        db.collection(COLLECTION)
            .orderBy(DATE, Query.Direction.DESCENDING) //최신 날짜 순으로 조회
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val result = ArrayList<Saying>()
                for (document in documents) {
                    //Log.d(TAG, "${document.id} => ${document.data}")

                    val saying = document.toObject<Saying>().apply {
                        imageRef = storageRef.child("images/${image}.jpg")
                        docId = document.id
                    }

                    Log.d(TAG, saying.toString())

                    result.add(saying)
//                    downloadUrl(document.data["image"].toString())
                }
                _sayingData.postValue(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun downloadUrl(fileName: String){

//        Glide.with(this).load(storage.reference.child("sd"))
        storage.reference.child("images/${fileName}.jpg").downloadUrl.addOnSuccessListener {
            // Got the download URL for 'users/me/profile.png'
            Log.d("URL", it.toString())
        }.addOnFailureListener {
            // Handle any errors
        }

    }
}