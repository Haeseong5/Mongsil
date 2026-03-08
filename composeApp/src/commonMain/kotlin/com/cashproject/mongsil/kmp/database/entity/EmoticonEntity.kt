package com.cashproject.mongsil.kmp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EmoticonEntity")
data class EmoticonEntity(
    @PrimaryKey
    val id: Long,
    val imageUrl: String,
    val title: String,
    val textColor: String,
    val backgroundColor: String,
    val lastUpdated: Long,
)
