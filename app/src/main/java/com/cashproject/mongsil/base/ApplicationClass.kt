package com.cashproject.mongsil.base

import android.app.Application
import android.content.SharedPreferences
import com.cashproject.mongsil.util.FILENAME

class ApplicationClass : Application(){

    companion object{
        lateinit var prefs: SharedPreferences
        const val COLLECTION = "Mongsil"
        const val DATE = "date"
    }

    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences(FILENAME, 0)

    }
}