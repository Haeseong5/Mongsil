package com.cashproject.mongsil.data.firebase.fcm

import kotlinx.serialization.Serializable

@Serializable
data class AppVersion(
    val latestAppVersionCode: Int = 0,
    val latestAppVersionName: String = "-",
)