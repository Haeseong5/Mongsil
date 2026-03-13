package com.cashproject.mongsil.kmp.model

enum class ScreenLockMethod(val key: String) {
    NONE("NONE"),
    SYSTEM("SYSTEM"),
    APP_PASSWORD("APP_PASSWORD");

    companion object {
        fun fromKey(key: String): ScreenLockMethod =
            entries.find { it.key == key } ?: NONE
    }
}
