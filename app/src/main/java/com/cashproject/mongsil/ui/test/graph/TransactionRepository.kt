package com.cashproject.mongsil.ui.test.graph

import com.google.gson.JsonParser

class TransactionRepository(
    private val transactionService: TransactionService = TransactionService()
) {

    fun filterTransaction(): List<Transaction> {
        return transactionService.getAllTransaction().subList(0, 7)
    }


}

fun isValidJson(json: String?): Boolean {
    return try {
        val parser = JsonParser()
        parser.parse(json)
        true
    } catch (e: Exception) {
        false
    }
}