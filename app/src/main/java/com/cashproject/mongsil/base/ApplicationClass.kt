package com.cashproject.mongsil.base

import android.app.Application
import android.content.SharedPreferences
import com.cashproject.mongsil.util.FILENAME

class ApplicationClass : Application(){

    companion object{
        lateinit var prefs: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences(FILENAME, 0)

    }
}