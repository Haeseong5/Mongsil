package com.cashproject.mongsil.base

import android.app.Application
import android.content.SharedPreferences
import com.cashproject.mongsil.R
import com.cashproject.mongsil.model.data.Emoticon
import com.cashproject.mongsil.model.data.Emoticons.emoticons
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
        initEmoticons()
    }

    private fun initEmoticons(){
        emoticons.add(
            Emoticon(
            0,
            "행복",
            R.drawable.emoticon_01_happy,
            R.color.emoticon_happy_text,
            R.color.emoticon_happy_background
        )
        )

        emoticons.add(
            Emoticon(
            1,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        )
        )

        emoticons.add(
            Emoticon(
            2,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        )
        )

        emoticons.add(
            Emoticon(
            3,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        )
        )

        emoticons.add(
            Emoticon(
            4,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        )
        )


        emoticons.add(
            Emoticon(
            5,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        )
        )
    }
}