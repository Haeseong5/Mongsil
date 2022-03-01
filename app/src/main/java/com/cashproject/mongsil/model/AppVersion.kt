package com.cashproject.mongsil.model

import kotlinx.serialization.Serializable

@Serializable
data class AppVersion(
    val latestAppVersionCode: Int = 0,
    val latestAppVersionName: String = "-",
)