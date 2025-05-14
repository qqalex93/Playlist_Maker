package com.practicum.playlistmaker.di

import android.content.res.Configuration
import com.practicum.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.repository.TrackRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import com.practicum.playlistmaker.app.App.Companion.DI_HISTORY_SHARED_PREFERENCES
import com.practicum.playlistmaker.app.App.Companion.DI_SETTINGS_SHARED_PREFERENCES
import com.practicum.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.repository.AudioPlayerRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.repository.SettingsRepository
import com.practicum.playlistmaker.sharing.data.ExternalActionsImpl
import com.practicum.playlistmaker.sharing.domain.api.repository.ExternalActions
import org.koin.android.ext.koin.androidApplication

val repositoryModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(
            networkClient = get(),
            sharedPreferences = get(named(DI_HISTORY_SHARED_PREFERENCES)),
            gson = get()
        )
    }
    single<SettingsRepository> {
        val isDefaultThemeDark = androidApplication().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        SettingsRepositoryImpl(
            isDefaultThemeDark,
            sharedPreferences = get(named(DI_SETTINGS_SHARED_PREFERENCES))
        )
    }

    single<ExternalActions> {
        ExternalActionsImpl(androidApplication())
    }

    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(player = get())
    }
}