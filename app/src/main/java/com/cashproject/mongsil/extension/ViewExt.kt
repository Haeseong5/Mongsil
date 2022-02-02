package com.cashproject.mongsil.extension

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.cashproject.mongsil.util.LiveEvent
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

fun showSnack(view: View, msg : String){
    Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("RETRY",null).show()
}

//fun showToast(context : Context, resourceId:String){
//    Toast.makeText(context,resourceId,Toast.LENGTH_SHORT).show()
//}

fun Context.showToast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable =
        apply { compositeDisposable.add(this) }

fun Bitmap.saveImage(context: Context): Uri? {
    if (android.os.Build.VERSION.SDK_INT >= 29) {
        val values = ContentValues() //ContentsValues() 객체는 ContentResolver 가 처리 할수 있는 값 집합을 저장하는데 사용
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/몽실")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")

        //ContentsResolver.insert() : contentValues() 객체와 uri 를 ContentProvider 에게 전달.
        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(this, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
            return uri
        }
    } else { //API 28 이하
        val directory =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + separator + "몽실")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileName =  "img_${SystemClock.uptimeMillis()}"+ ".jpeg"
        val file = File(directory, fileName)
        saveImageToStream(this, FileOutputStream(file))
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DATA, file.absolutePath)
        // .DATA is deprecated in API 29
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        return Uri.fromFile(file)
    }
    return null
}

fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
    if (outputStream != null) {
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) //quality:100 비손실 압축
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun getImageUri(context: Context, bitmap: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

    //    java.lang.SecurityException: Permission Denial: writing com.android.providers.media.MediaProvider uri content://media/external/images/media from pid=22556, uid=10210 requires android.permission.WRITE_EXTERNAL_STORAGE, or grantUriPermission()
    //  java.lang.IllegalStateException: MediaStore.Images.Media.…gsil",
    //            null
    //        ) must not be null
    val path: String = MediaStore.Images.Media.insertImage(
        context.contentResolver,
        bitmap,
        "mongsil",
        null
    )
    return Uri.parse(path)
}

/**
 * RxBinding의 Throttle 기능 사용하는 Button 함수
 * @param throttleSecond 해당 시간동안 중복 클릭 방지 (기본으로 1초)
 * @param subscribe 클릭 리스너 정의
 */
fun Button.onThrottleClick(throttleSecond: Long = 1, subscribe: (() -> Unit)? = null) = clicks()
    .throttleFirst(throttleSecond, TimeUnit.SECONDS)
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe { subscribe?.invoke() }


fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}