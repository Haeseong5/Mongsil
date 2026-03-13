package com.cashproject.mongsil.kmp.screen.setting

class DesktopDiaryReminderScheduler : DiaryReminderScheduler {
    override suspend fun setEnabled(enabled: Boolean): Boolean = false
}
