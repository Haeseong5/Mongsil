package com.cashproject.mongsil.base

import android.app.Application
import android.content.SharedPreferences
import com.cashproject.mongsil.manager.setChildAdmobMode
import com.cashproject.mongsil.model.AppVersion
import com.cashproject.mongsil.util.FILENAME


class ApplicationClass : Application() {

    companion object {
        lateinit var prefs: SharedPreferences
        const val DATE = "date"
        var appVersion: AppVersion = AppVersion()
    }

    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences(FILENAME, 0)
        setChildAdmobMode()
    }
}