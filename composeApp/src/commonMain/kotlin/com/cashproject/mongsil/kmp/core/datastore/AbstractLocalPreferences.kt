package com.cashproject.mongsil.kmp.core.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Flow 기반 반응형 로직을 공유하는 추상 베이스 클래스.
 * 플랫폼별 저장소(SharedPreferences / NSUserDefaults) 연동은 실제 구현 클래스에서 담당.
 */
abstract class AbstractLocalPreferences : LocalPreferences {

    private val flows = HashMap<String, MutableStateFlow<Any?>>()

    protected fun getOrCreateFlow(key: String, loader: () -> Any?): MutableStateFlow<Any?> =
        flows.getOrPut(key) { MutableStateFlow(loader()) }

    protected suspend fun updateFlow(key: String, value: Any?) {
        val existing = flows[key]
        if (existing != null) {
            existing.emit(value)
        } else {
            flows[key] = MutableStateFlow(value)
        }
    }

    // ---- get ----

    override fun getBoolean(key: String): Flow<Boolean?> =
        getOrCreateFlow(key) { loadBoolean(key) }.map { it as? Boolean }

    override fun getInt(key: String): Flow<Int?> =
        getOrCreateFlow(key) { loadInt(key) }.map { it as? Int }

    override fun getLong(key: String): Flow<Long?> =
        getOrCreateFlow(key) { loadLong(key) }.map { it as? Long }

    override fun getFloat(key: String): Flow<Float?> =
        getOrCreateFlow(key) { loadFloat(key) }.map { it as? Float }

    override fun getDouble(key: String): Flow<Double?> =
        getOrCreateFlow(key) { loadDouble(key) }.map { it as? Double }

    override fun getString(key: String): Flow<String?> =
        getOrCreateFlow(key) { loadString(key) }.map { it as? String }

    @Suppress("UNCHECKED_CAST")
    override fun getStringSet(key: String): Flow<Set<String>?> =
        getOrCreateFlow(key) { loadStringSet(key) }.map { it as? Set<String> }

    override fun getByteArray(key: String): Flow<ByteArray?> =
        getOrCreateFlow(key) { loadByteArray(key) }.map { it as? ByteArray }

    // ---- set ----

    override suspend fun setBoolean(key: String, value: Boolean) {
        saveBoolean(key, value)
        updateFlow(key, value)
    }

    override suspend fun setInt(key: String, value: Int) {
        saveInt(key, value)
        updateFlow(key, value)
    }

    override suspend fun setLong(key: String, value: Long) {
        saveLong(key, value)
        updateFlow(key, value)
    }

    override suspend fun setFloat(key: String, value: Float) {
        saveFloat(key, value)
        updateFlow(key, value)
    }

    override suspend fun setDouble(key: String, value: Double) {
        saveDouble(key, value)
        updateFlow(key, value)
    }

    override suspend fun setString(key: String, value: String) {
        saveString(key, value)
        updateFlow(key, value)
    }

    override suspend fun setStringSet(key: String, value: Set<String>) {
        saveStringSet(key, value)
        updateFlow(key, value)
    }

    override suspend fun setByteArray(key: String, value: ByteArray) {
        saveByteArray(key, value)
        updateFlow(key, value)
    }

    // ---- abstract platform operations ----

    protected abstract fun loadBoolean(key: String): Boolean?
    protected abstract fun loadInt(key: String): Int?
    protected abstract fun loadLong(key: String): Long?
    protected abstract fun loadFloat(key: String): Float?
    protected abstract fun loadDouble(key: String): Double?
    protected abstract fun loadString(key: String): String?
    protected abstract fun loadStringSet(key: String): Set<String>?
    protected abstract fun loadByteArray(key: String): ByteArray?

    protected abstract fun saveBoolean(key: String, value: Boolean)
    protected abstract fun saveInt(key: String, value: Int)
    protected abstract fun saveLong(key: String, value: Long)
    protected abstract fun saveFloat(key: String, value: Float)
    protected abstract fun saveDouble(key: String, value: Double)
    protected abstract fun saveString(key: String, value: String)
    protected abstract fun saveStringSet(key: String, value: Set<String>)
    protected abstract fun saveByteArray(key: String, value: ByteArray)
}
