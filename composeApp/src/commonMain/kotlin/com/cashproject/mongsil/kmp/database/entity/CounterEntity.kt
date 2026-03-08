package com.cashproject.mongsil.kmp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CounterEntity")
data class CounterEntity(
    @PrimaryKey
    val id: Long = 1,
    val count: Long,
    val lastUpdated: Long,
)
