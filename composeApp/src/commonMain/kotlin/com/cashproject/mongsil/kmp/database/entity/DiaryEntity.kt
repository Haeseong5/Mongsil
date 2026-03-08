package com.cashproject.mongsil.kmp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DiaryEntity")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String,
    val emoticonId: Long? = null,
    val photoUri: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
)
