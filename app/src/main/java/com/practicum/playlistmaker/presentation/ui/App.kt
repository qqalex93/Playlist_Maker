package com.practicum.playlistmaker.presentation.ui

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.interactor.SettingsInteractor

class App : Application() {

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        Creator.initApplication(this)
        Creator.initApplication(this)
        settingsInteractor = Creator.provideSettingsInteractor()
        setSavedTheme()
    }

    private fun setSavedTheme() {
        val isDarkModeOn = settingsInteractor.getTheme()
        switchTheme(isDarkModeOn)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        settingsInteractor.saveTheme(darkThemeEnabled)
    }

    companion object {
        const val BAD_REQUEST_CODE = 400
        const val SUCCESS_CODE = 200
        const val ERROR_SERVER_CODE = 500
        const val TRACK_KEY = "track"
    }
}