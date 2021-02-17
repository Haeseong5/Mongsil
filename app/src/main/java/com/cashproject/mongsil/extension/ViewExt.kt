package com.cashproject.mongsil.extension

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun showSnack(view: View, msg : String){
    Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("RETRY",null).show()
}

//fun showToast(context : Context, resourceId:String){
//    Toast.makeText(context,resourceId,Toast.LENGTH_SHORT).show()
//}

fun Context.showToast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable =
        apply { compositeDisposable.add(this) }