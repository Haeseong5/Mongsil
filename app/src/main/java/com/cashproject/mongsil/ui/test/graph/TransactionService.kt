package com.cashproject.mongsil.ui.test.graph

import android.content.Context
import android.util.Log
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.App
import com.cashproject.mongsil.extension.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream

class TransactionService(
    private val context: Context = App.context
) {
    @OptIn(ExperimentalSerializationApi::class)
    fun getAllTransaction(): List<Transaction> {
        val inputStream = context.resources.openRawResource(R.raw.jsonformatter)
        val transactions = json.decodeFromStream<List<Transaction>>(inputStream)
        Log.d("TransactionRepository", transactions.toString())
        return transactions
    }
}