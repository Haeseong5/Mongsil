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
    @ColumnInfo
    val date: Date,
    @ColumnInfo
    val docId: String
)