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
            R.color.emoticon_01_happy_text,
            R.color.emoticon_01_happy_background))

        emoticons.add(
            Emoticon(
            1,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_02_enjoy_text,
            R.color.emoticon_02_enjoy_background))

        emoticons.add(
            Emoticon(
            2,
            "만족",
            R.drawable.emoticon_03,
            R.color.emoticon_03_satisfied_text,
            R.color.emoticon_03_satisfied_background))

        emoticons.add(
            Emoticon(
            3,
            "보통",
            R.drawable.emoticon_04,
            R.color.emoticon_04_normal_text,
            R.color.emoticon_04_normal_background))

        emoticons.add(
            Emoticon(
            4,
            "피곤",
            R.drawable.emoticon_05,
            R.color.emoticon_05_tired_text,
            R.color.emoticon_05_tired_background))


        emoticons.add(
            Emoticon(
            5,
            "창피",
            R.drawable.emoticon_06,
            R.color.emoticon_06_shame_text,
            R.color.emoticon_06_shame_background))
        emoticons.add(
            Emoticon(
                6,
                "지루함",
                R.drawable.emoticon_07,
                R.color.emoticon_07_boring_text,
                R.color.emoticon_07_boring_background))
        emoticons.add(
            Emoticon(
                7,
                "화남",
                R.drawable.emoticon_08,
                R.color.emoticon_08_angry_text,
                R.color.emoticon_08_angry_background))
        emoticons.add(
            Emoticon(
                8,
                "불쾌",
                R.drawable.emoticon_09,
                R.color.emoticon_09_discomfort_text,
                R.color.emoticon_09_discomfort_background))
        emoticons.add(
            Emoticon(
                9,
                "실망",
                R.drawable.emoticon_10,
                R.color.emoticon_10_disappoint_text,
                R.color.emoticon_10_disappoint_background))
        emoticons.add(
            Emoticon(
                10,
                "불안",
                R.drawable.emoticon_11,
                R.color.emoticon_11_unrest_text,
                R.color.emoticon_11_unrest_background))
        emoticons.add(
            Emoticon(
                11,
                "우울",
                R.drawable.emoticon_12,
                R.color.emoticon_12_depressed_text,
                R.color.emoticon_12_depressed_background))
        emoticons.add(
            Emoticon(
                12,
                "슬픔",
                R.drawable.emoticon_13,
                R.color.emoticon_13_sad_text,
                R.color.emoticon_13_sad_background))
        emoticons.add(
            Emoticon(
                13,
                "놀람",
                R.drawable.emoticon_14,
                R.color.emoticon_14_surprise_text,
                R.color.emoticon_14_surprise_background))
        emoticons.add(
            Emoticon(
                14,
                "외로움",
                R.drawable.emoticon_15,
                R.color.emoticon_15_lonely_text,
                R.color.emoticon_15_lonely_background))
    }
}