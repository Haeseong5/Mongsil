package com.cashproject.mongsil.model.db.datasource

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreDataSource {

    val db by lazy {
        FirebaseFirestore.getInstance()
    }

}