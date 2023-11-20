package com.cashproject.mongsil.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.cashproject.mongsil.manager.setChildAdmobMode
import com.cashproject.mongsil.data.firebase.fcm.AppVersion
import com.cashproject.mongsil.util.FILENAME
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var prefs: SharedPreferences
        const val DATE = "date"
        var appVersion: AppVersion = AppVersion()

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: App
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        instance = this
        prefs = getSharedPreferences(FILENAME, 0)
        setChildAdmobMode()
    }
}