package com.cashproject.mongsil.model.data

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Keep
@Parcelize
@Serializable
@Entity
data class Saying(
    @PrimaryKey
    @SerialName("id")
    val docId: String = "",
    @ColumnInfo
    val image: String = "",
    @ColumnInfo(name = "s")
    val squareImage: String = "",

    @Deprecated("사용되지 않는 필드입니다.")
    @Contextual
    @ColumnInfo
    val date: Date = Date(),

    val test: String
) : Parcelable