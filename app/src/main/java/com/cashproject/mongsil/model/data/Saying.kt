package com.cashproject.mongsil.model.data

import com.google.firebase.Timestamp
import com.google.firebase.storage.StorageReference

data class Saying(
    var docId: String? = null,
    var image: String? = null,
    var imageRef: StorageReference? = null,
    var date: Timestamp? = null)
