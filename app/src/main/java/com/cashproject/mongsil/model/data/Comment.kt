package com.cashproject.mongsil.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    val content: String,
    @ColumnInfo
    val emotion: Int,
    @ColumnInfo
    val time: Date,

    @Deprecated("사용되지 않는 필드입니다. 추후 데이터베이스 마이그레이션이 필요합니다.")
    @ColumnInfo
    val date: Date = Date(),
    @Deprecated("사용되지 않는 필드입니다. 추후 데이터베이스 마이그레이션이 필요합니다.")
    @ColumnInfo
    val docId: String = ""
)