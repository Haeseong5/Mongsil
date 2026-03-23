package com.cashproject.mongsil.kmp.core.backup.model

import kotlinx.serialization.Serializable

@Serializable
data class BackupManifest(
    val formatVersion: Int = CURRENT_FORMAT_VERSION,
    val appVersion: String,
    val createdAtIso: String,
    val platformName: String,
    val diaryCount: Int,
    val diaries: List<BackupDiary>,
) {
    companion object {
        const val CURRENT_FORMAT_VERSION = 1
    }
}
