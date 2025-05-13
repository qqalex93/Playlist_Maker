package com.practicum.playlistmaker.app

import android.app.Application
import android.content.res.Configuration
import com.practicum.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        Creator.initDefaultTheme(
            resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        )

        val settingsInteractor = Creator.provideSettingsInteractor()
        settingsInteractor.setSavedTheme()
    }

    companion object {
        const val TRACK_KEY = "track"
    }
}