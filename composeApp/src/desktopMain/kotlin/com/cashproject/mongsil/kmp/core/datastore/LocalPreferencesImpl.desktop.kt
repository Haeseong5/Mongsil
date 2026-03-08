package com.cashproject.mongsil.kmp.core.datastore

import java.util.Base64
import java.util.prefs.Preferences

class LocalPreferencesImpl(name: String) : AbstractLocalPreferences() {

    private val prefs: Preferences = Preferences.userRoot().node(name)

    override fun loadBoolean(key: String): Boolean? =
        if (prefs.get(key, null) != null) prefs.getBoolean(key, false) else null

    override fun loadInt(key: String): Int? =
        if (prefs.get(key, null) != null) prefs.getInt(key, 0) else null

    override fun loadLong(key: String): Long? =
        if (prefs.get(key, null) != null) prefs.getLong(key, 0L) else null

    override fun loadFloat(key: String): Float? =
        if (prefs.get(key, null) != null) prefs.getFloat(key, 0f) else null

    override fun loadDouble(key: String): Double? =
        if (prefs.get(key, null) != null) prefs.getDouble(key, 0.0) else null

    override fun loadString(key: String): String? = prefs.get(key, null)

    override fun loadStringSet(key: String): Set<String>? {
        val raw = prefs.get(key, null) ?: return null
        return raw.split("\u0000").toSet()
    }

    override fun loadByteArray(key: String): ByteArray? {
        val raw = prefs.get(key, null) ?: return null
        return Base64.getDecoder().decode(raw)
    }

    override fun saveBoolean(key: String, value: Boolean) = prefs.putBoolean(key, value)
    override fun saveInt(key: String, value: Int) = prefs.putInt(key, value)
    override fun saveLong(key: String, value: Long) = prefs.putLong(key, value)
    override fun saveFloat(key: String, value: Float) = prefs.putFloat(key, value)
    override fun saveDouble(key: String, value: Double) = prefs.putDouble(key, value)
    override fun saveString(key: String, value: String) = prefs.put(key, value)

    override fun saveStringSet(key: String, value: Set<String>) =
        prefs.put(key, value.joinToString("\u0000"))

    override fun saveByteArray(key: String, value: ByteArray) =
        prefs.put(key, Base64.getEncoder().encodeToString(value))
}
