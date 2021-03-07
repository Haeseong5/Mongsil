package com.cashproject.mongsil.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val date: Date,
    @ColumnInfo
    val comment: String,
    @ColumnInfo
    val emoticon: Int
)