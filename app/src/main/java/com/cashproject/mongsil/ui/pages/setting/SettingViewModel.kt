package com.cashproject.mongsil.ui.pages.setting

import androidx.lifecycle.ViewModel
import com.cashproject.mongsil.base.ApplicationClass
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class SettingViewModel : ViewModel() {
    val storage = Firebase.storage("gs://mongsil-8dc44.appspot.com")
    var storageRef: StorageReference = storage.getReference()
    var backupRef: StorageReference? = storageRef.child("backup")

}