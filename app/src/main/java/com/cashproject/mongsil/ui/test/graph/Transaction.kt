package com.cashproject.mongsil.ui.test.graph

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val amount: Float,
    val name: String,
    val timestamp: String,
    val type: String,
)
