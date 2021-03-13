package com.cashproject.mongsil.model.data

import androidx.navigation.NavType
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.storage.StorageReference
import java.util.*

@Entity
data class Saying(
    @PrimaryKey var docId: String,
    @ColumnInfo var image: String,
    @ColumnInfo var date: Date){

    constructor(): this("", "", Date())
}
