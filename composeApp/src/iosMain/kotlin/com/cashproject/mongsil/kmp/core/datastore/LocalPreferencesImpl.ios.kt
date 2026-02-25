package com.cashproject.mongsil.kmp.core.datastore

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import platform.Foundation.NSUserDefaults

/**
 * iOS 구현체.
 * NSUserDefaults로 영속화하며, 앱 재시작 후에도 값을 유지한다.
 * StringSet은 NSArray(List<String>)로 저장.
 * ByteArray는 Base64 인코딩된 String으로 저장.
 */
class LocalPreferencesImpl(
    name: String,
) : AbstractLocalPreferences() {

    private val userDefaults: NSUserDefaults = NSUserDefaults(suiteName = name)

    private fun hasKey(key: String): Boolean = userDefaults.objectForKey(key) != null

    override fun loadBoolean(key: String): Boolean? =
        if (hasKey(key)) userDefaults.boolForKey(key) else null

    override fun loadInt(key: String): Int? =
        if (hasKey(key)) userDefaults.integerForKey(key).toInt() else null

    override fun loadLong(key: String): Long? =
        if (hasKey(key)) userDefaults.integerForKey(key) else null

    override fun loadFloat(key: String): Float? =
        if (hasKey(key)) userDefaults.floatForKey(key) else null

    override fun loadDouble(key: String): Double? =
        if (hasKey(key)) userDefaults.doubleForKey(key) else null

    override fun loadString(key: String): String? =
        userDefaults.stringForKey(key)

    override fun loadStringSet(key: String): Set<String>? =
        userDefaults.arrayForKey(key)?.filterIsInstance<String>()?.toSet()

    @OptIn(ExperimentalEncodingApi::class)
    override fun loadByteArray(key: String): ByteArray? =
        userDefaults.stringForKey(key)?.let { Base64.decode(it) }

    override fun saveBoolean(key: String, value: Boolean) =
        userDefaults.setBool(value, forKey = key)

    override fun saveInt(key: String, value: Int) =
        userDefaults.setInteger(value.toLong(), forKey = key)

    override fun saveLong(key: String, value: Long) =
        userDefaults.setInteger(value, forKey = key)

    override fun saveFloat(key: String, value: Float) =
        userDefaults.setFloat(value, forKey = key)

    override fun saveDouble(key: String, value: Double) =
        userDefaults.setDouble(value, forKey = key)

    override fun saveString(key: String, value: String) =
        userDefaults.setObject(value, forKey = key)

    override fun saveStringSet(key: String, value: Set<String>) =
        userDefaults.setObject(value.toList(), forKey = key)

    @OptIn(ExperimentalEncodingApi::class)
    override fun saveByteArray(key: String, value: ByteArray) =
        userDefaults.setObject(Base64.encode(value), forKey = key)
}
