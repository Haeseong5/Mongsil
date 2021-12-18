package com.cashproject.mongsil.model.data

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Keep
@Parcelize
@Entity
data class Saying(
    @PrimaryKey var docId: String,
    @ColumnInfo var image: String? = null,
    @ColumnInfo var s: String, //square image
    @ColumnInfo var date: Date //deprecated
): Parcelable {
    constructor() : this("", "", "defaultImageUrl", Date())
}
