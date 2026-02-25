package com.cashproject.mongsil.kmp.core.datastore

import android.content.Context
import android.content.SharedPreferences
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Android 구현체.
 * SharedPreferences로 영속화하며, 앱 재시작 후에도 값을 유지한다.
 * Double은 SharedPreferences가 미지원하므로 Long 비트로 변환하여 저장.
 * ByteArray는 Base64 인코딩된 String으로 저장.
 */
class LocalPreferencesImpl(
    private val name: String,
) : AbstractLocalPreferences(), KoinComponent {

    private val context: Context by inject()

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    override fun loadBoolean(key: String): Boolean? =
        if (prefs.contains(key)) prefs.getBoolean(key, false) else null

    override fun loadInt(key: String): Int? =
        if (prefs.contains(key)) prefs.getInt(key, 0) else null

    override fun loadLong(key: String): Long? =
        if (prefs.contains(key)) prefs.getLong(key, 0L) else null

    override fun loadFloat(key: String): Float? =
        if (prefs.contains(key)) prefs.getFloat(key, 0f) else null

    override fun loadDouble(key: String): Double? =
        if (prefs.contains(key)) Double.fromBits(prefs.getLong(key, 0L)) else null

    override fun loadString(key: String): String? =
        prefs.getString(key, null)

    override fun loadStringSet(key: String): Set<String>? =
        prefs.getStringSet(key, null)?.toSet()

    @OptIn(ExperimentalEncodingApi::class)
    override fun loadByteArray(key: String): ByteArray? =
        prefs.getString(key, null)?.let { Base64.decode(it) }

    override fun saveBoolean(key: String, value: Boolean) =
        prefs.edit().putBoolean(key, value).apply()

    override fun saveInt(key: String, value: Int) =
        prefs.edit().putInt(key, value).apply()

    override fun saveLong(key: String, value: Long) =
        prefs.edit().putLong(key, value).apply()

    override fun saveFloat(key: String, value: Float) =
        prefs.edit().putFloat(key, value).apply()

    override fun saveDouble(key: String, value: Double) =
        prefs.edit().putLong(key, value.toBits()).apply()

    override fun saveString(key: String, value: String) =
        prefs.edit().putString(key, value).apply()

    override fun saveStringSet(key: String, value: Set<String>) =
        prefs.edit().putStringSet(key, value).apply()

    @OptIn(ExperimentalEncodingApi::class)
    override fun saveByteArray(key: String, value: ByteArray) =
        prefs.edit().putString(key, Base64.encode(value)).apply()
}
