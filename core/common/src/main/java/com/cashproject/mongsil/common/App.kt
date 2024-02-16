package com.cashproject.mongsil.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences


class App : Application() {

    companion object {
        //TODO 이관
        lateinit var prefs: SharedPreferences
//        var appVersion: AppVersion = AppVersion()

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

        //TODO 이관
        prefs = getSharedPreferences("prefs", 0)

        //TODO mAINACTIVITY로 호출
//        setChildAdmobMode()
    }
}