package com.cashproject.mongsil.kmp.core.backup.model

import kotlinx.serialization.Serializable

@Serializable
data class BackupDiary(
    val date: String,
    val content: String,
    val emoticonId: Long? = null,
    val textAlign: String = "start",
    val textColor: String = "FF000000",
    val backgroundColor: String = "00000000",
    val createdAt: Long,
    val updatedAt: Long,
    val photoBase64: String? = null,
    val photoExtension: String? = null,
)
