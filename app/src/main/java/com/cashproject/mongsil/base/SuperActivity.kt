package com.cashproject.mongsil.base

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class SuperActivity : AppCompatActivity() {

    val TAG: String = this.javaClass.simpleName

    fun printLog(message: String) {
        Log.d(TAG, "@@@ $message")
    }

    fun printErrorLog(message: String) {
        Log.e(TAG, message)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "++onCreate()")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "++onStart()")
    }

    override fun onResume() {
        Log.d(TAG, "++onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "++onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "++onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "++onDestroy()")
        super.onDestroy()
    }

}