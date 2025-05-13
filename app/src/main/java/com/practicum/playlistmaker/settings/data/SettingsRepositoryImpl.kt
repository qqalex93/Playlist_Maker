package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.api.repository.SettingsRepository
import androidx.core.content.edit

class SettingsRepositoryImpl(
    private val isDefaultThemeDark: Boolean,
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, isDefaultThemeDark)
    }

    override fun saveTheme(isDarkModeOn: Boolean) {
        sharedPreferences.edit() {
            putBoolean(DARK_THEME_KEY, isDarkModeOn)
        }
    }

    override fun setSavedTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (getTheme()) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    companion object {
        private const val DARK_THEME_KEY = "is_dark_theme_on"
    }
}