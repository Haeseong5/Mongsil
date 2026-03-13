package com.cashproject.mongsil.kmp.screen.setting

interface DiaryReminderScheduler {
    suspend fun setEnabled(enabled: Boolean): Boolean
}
