package com.cashproject.mongsil.ui.test.pointer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class PointerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleVerticalList()
        }
    }

}