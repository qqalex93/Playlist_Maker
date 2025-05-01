package com.practicum.playlistmaker.data.themeSettings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.practicum.playlistmaker.domain.api.repository.SettingsRepository
import androidx.core.content.edit

class SettingsRepositoryImpl(private val application: Application) : SettingsRepository {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        SHARED_PREFERENCES, Context.MODE_PRIVATE
    )

    override fun getTheme(): Boolean {
        val darkModeOn = application.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        return sharedPreferences.getBoolean(DARK_THEME_KEY, darkModeOn)
    }

    override fun savedTheme(isDarkModeOn: Boolean) {
        sharedPreferences.edit() {
            putBoolean(DARK_THEME_KEY, isDarkModeOn)
        }
    }

    companion object {
        private const val DARK_THEME_KEY = "dark_theme_mode"
        private const val SHARED_PREFERENCES = "shared_preferences"
    }

}