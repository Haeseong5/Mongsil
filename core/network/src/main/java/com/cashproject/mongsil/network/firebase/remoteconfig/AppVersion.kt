package com.cashproject.mongsil.network.firebase.remoteconfig

import kotlinx.serialization.Serializable

@Serializable
data class AppVersion(
    val latestAppVersionCode: Int = 0,
    val latestAppVersionName: String = "-",
)