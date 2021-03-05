package com.cashproject.mongsil.util.image

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import java.io.InputStream

//https://muyu.tistory.com/entry/android-Firebase-Storage-%EC%82%AC%EC%9A%A9%ED%95%B4%EB%B3%B4%EA%B8%B0-Download-with-Glide
@GlideModule
class MyAppGildeModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(
            StorageReference::class.java,
            InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}

