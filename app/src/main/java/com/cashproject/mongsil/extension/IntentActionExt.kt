package com.cashproject.mongsil.extension

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

fun Fragment.intentAction(kClass: KClass<out Activity>) {
    startActivity(
        Intent(activity, kClass.java)
    )
}