package com.cashproject.mongsil.kmp.core.datastore.impl

import com.cashproject.mongsil.kmp.core.datastore.LocalPreferences
import com.cashproject.mongsil.kmp.core.datastore.SettingsPreferenceDataSource
import com.cashproject.mongsil.kmp.core.datastore.KEY_FONT_SCALE
import com.cashproject.mongsil.kmp.core.datastore.KEY_FONT_STYLE_OPTION
import com.cashproject.mongsil.kmp.core.datastore.KEY_IS_DARK_THEME
import com.cashproject.mongsil.kmp.core.datastore.KEY_IS_DIARY_REMINDER_ENABLED
import com.cashproject.mongsil.kmp.core.datastore.KEY_IS_SCREEN_LOCK_ENABLED
import com.cashproject.mongsil.kmp.core.datastore.KEY_SCREEN_LOCK_METHOD
import com.cashproject.mongsil.kmp.core.datastore.KEY_SCREEN_LOCK_PASSWORD_HASH
import com.cashproject.mongsil.kmp.core.datastore.KEY_THEME_MODE
import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.model.ScreenLockMethod
import com.cashproject.mongsil.kmp.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultSettingsPreferenceDataSource(
    private val localPreferences: LocalPreferences,
) : SettingsPreferenceDataSource {

    override val isDarkTheme: Flow<Boolean> = localPreferences
        .getBoolean(KEY_IS_DARK_THEME)
        .map { it ?: false }

    override val themeMode: Flow<ThemeMode> = localPreferences
        .getString(KEY_THEME_MODE)
        .map { ThemeMode.fromKey(it ?: ThemeMode.SYSTEM.key) }

    override val fontStyleOption: Flow<FontStyleOption> = localPreferences
        .getString(KEY_FONT_STYLE_OPTION)
        .map { FontStyleOption.fromKey(it ?: FontStyleOption.SYSTEM.key) }

    override val fontScale: Flow<Float> = localPreferences
        .getFloat(KEY_FONT_SCALE)
        .map { (it ?: DEFAULT_FONT_SCALE).coerceIn(MIN_FONT_SCALE, MAX_FONT_SCALE) }

    override fun getThemeModeSync(): ThemeMode =
        ThemeMode.fromKey(localPreferences.getStringSync(KEY_THEME_MODE) ?: ThemeMode.SYSTEM.key)

    override fun getFontStyleOptionSync(): FontStyleOption =
        FontStyleOption.fromKey(
            localPreferences.getStringSync(KEY_FONT_STYLE_OPTION) ?: FontStyleOption.SYSTEM.key
        )

    override fun getFontScaleSync(): Float =
        (localPreferences.getFloatSync(KEY_FONT_SCALE) ?: DEFAULT_FONT_SCALE)
            .coerceIn(MIN_FONT_SCALE, MAX_FONT_SCALE)

    override val isDiaryReminderEnabled: Flow<Boolean> = localPreferences
        .getBoolean(KEY_IS_DIARY_REMINDER_ENABLED)
        .map { it ?: false }

    override val isScreenLockEnabled: Flow<Boolean> = localPreferences
        .getBoolean(KEY_IS_SCREEN_LOCK_ENABLED)
        .map { it ?: false }

    override val screenLockMethod: Flow<ScreenLockMethod> = localPreferences
        .getString(KEY_SCREEN_LOCK_METHOD)
        .map { ScreenLockMethod.fromKey(it ?: ScreenLockMethod.NONE.key) }

    override val screenLockPasswordHash: Flow<String?> = localPreferences
        .getString(KEY_SCREEN_LOCK_PASSWORD_HASH)
        .map { it?.takeIf(String::isNotBlank) }

    override suspend fun updateIsDarkTheme(isDarkTheme: Boolean) {
        localPreferences.setBoolean(KEY_IS_DARK_THEME, isDarkTheme)
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        localPreferences.setString(KEY_THEME_MODE, themeMode.key)
    }

    override suspend fun updateFontStyleOption(option: FontStyleOption) {
        localPreferences.setString(KEY_FONT_STYLE_OPTION, option.key)
    }

    override suspend fun updateFontScale(scale: Float) {
        localPreferences.setFloat(
            KEY_FONT_SCALE,
            scale.coerceIn(MIN_FONT_SCALE, MAX_FONT_SCALE)
        )
    }

    override suspend fun updateDiaryReminderEnabled(enabled: Boolean) {
        localPreferences.setBoolean(KEY_IS_DIARY_REMINDER_ENABLED, enabled)
    }

    override suspend fun updateScreenLockEnabled(enabled: Boolean) {
        localPreferences.setBoolean(KEY_IS_SCREEN_LOCK_ENABLED, enabled)
    }

    override suspend fun updateScreenLockMethod(method: ScreenLockMethod) {
        localPreferences.setString(KEY_SCREEN_LOCK_METHOD, method.key)
    }

    override suspend fun updateScreenLockPasswordHash(passwordHash: String?) {
        localPreferences.setString(KEY_SCREEN_LOCK_PASSWORD_HASH, passwordHash.orEmpty())
    }

    companion object {
        private const val DEFAULT_FONT_SCALE = 1f
        private const val MIN_FONT_SCALE = 0.8f
        private const val MAX_FONT_SCALE = 1.4f
    }
}
