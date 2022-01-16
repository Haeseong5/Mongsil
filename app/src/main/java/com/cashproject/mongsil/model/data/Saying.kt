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
    @ColumnInfo var image: String,
    @ColumnInfo var s: String, //square image
    @ColumnInfo var date: Date //deprecated
) : Parcelable {
    constructor() : this(
        "default",
        "https://mblogthumb-phinf.pstatic.net/20141227_166/studygir_14196771148363Yi0G_JPEG/bo99_%282%29.jpg?type=w2",
        "https://t1.daumcdn.net/cfile/tistory/2577184451E8D75605",
        Date()
    )
}
