package com.cashproject.mongsil.kmp.model

enum class ThemeMode(val key: String) {
    SYSTEM("SYSTEM"),
    LIGHT("LIGHT"),
    DARK("DARK");

    companion object {
        fun fromKey(key: String): ThemeMode = entries.find { it.key == key } ?: SYSTEM
    }
}
