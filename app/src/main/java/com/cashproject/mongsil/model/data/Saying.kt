package com.cashproject.mongsil.model.data

import androidx.navigation.NavType
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.storage.StorageReference
import java.util.*

data class Saying(
    var docId: String? = null,
    var image: String? = null,
    var date: Date? = null)
