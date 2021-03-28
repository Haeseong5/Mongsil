package com.cashproject.mongsil.model.data

import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.storage.StorageReference
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Saying(
    @PrimaryKey var docId: String,
    @ColumnInfo var image: String,
    @ColumnInfo var s: String,
    @ColumnInfo var date: Date) : Parcelable {

    constructor(): this("", "","defaultImageUrl", Date())
}
