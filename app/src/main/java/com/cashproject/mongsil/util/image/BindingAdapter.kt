package com.cashproject.mongsil.util.image

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.cashproject.mongsil.R
import com.google.firebase.storage.StorageReference


object BindingAdapter {

    // 코틀린 object 객체를 자바에서 사용할 경우, 코틀린의 변수 또는 함수를 가져다 쓰기 위해서 jvmStatic Anotation 을 사용
    //@JvmStatic은 static 변수의 get/set 함수를 자동으로 만들라는 의미
    //코틀린의 static 변수를 자바 호환성을 위해 사용
    //바인딩어댑터는 적절한 프레임워크를 호출하여 값을 설정하는 작업을 담당
    @JvmStatic
    @BindingAdapter("bindImage")
    fun bindImage(view: ImageView, imageUrl: String?) {
        GlideApp.with(view.context)
            .load(imageUrl)
            .centerCrop()
//            .placeholder(R.drawable.loading)
            .error(R.drawable.ic_baseline_terrain_24)
            .into(view)
    }

}
