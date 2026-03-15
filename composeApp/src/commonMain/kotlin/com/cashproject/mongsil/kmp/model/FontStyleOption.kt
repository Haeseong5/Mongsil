package com.cashproject.mongsil.kmp.model

enum class FontStyleOption(val key: String) {
    SYSTEM("SYSTEM"),
    GAMJA_FLOWER("GAMJA_FLOWER");

    companion object {
        fun fromKey(key: String): FontStyleOption =
            entries.find { it.key == key } ?: GAMJA_FLOWER
    }
}
