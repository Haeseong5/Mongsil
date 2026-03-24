package com.cashproject.mongsil.kmp.core.backup

import com.cashproject.mongsil.kmp.core.backup.model.BackupManifest
import kotlinx.serialization.json.Json

class BackupSerializer {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun serialize(manifest: BackupManifest): ByteArray {
        val jsonString = json.encodeToString(BackupManifest.serializer(), manifest)
        return jsonString.encodeToByteArray()
    }

    fun deserialize(bytes: ByteArray): BackupManifest {
        val jsonString = bytes.decodeToString()
        return json.decodeFromString(BackupManifest.serializer(), jsonString)
    }
}
