package com.practicum.playlistmaker.app

import android.app.Application
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModeModel
import com.practicum.playlistmaker.settings.domain.api.interactor.SettingsInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModeModel)
        }

        val settingsInteractor: SettingsInteractor = getKoin().get()
        settingsInteractor.setSavedTheme()
    }

    companion object {
        const val DI_SETTINGS_SHARED_PREFERENCES = "settings_shared_preference"
        const val DI_HISTORY_SHARED_PREFERENCES = "history_shared_preference"
        const val TRACK_KEY = "track"
    }
}