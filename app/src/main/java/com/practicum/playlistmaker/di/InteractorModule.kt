package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.history.domain.api.interactor.TrackHistoryInteractor
import com.practicum.playlistmaker.history.domain.impl.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.player.domain.api.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.api.interactor.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.impl.TrackSearchInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.interactor.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.interactor.ExternalActionsInteractor
import com.practicum.playlistmaker.sharing.domain.impl.ExternalActionsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<TrackSearchInteractor> {
        TrackSearchInteractorImpl(trackRepository = get())
    }

    single<TrackHistoryInteractor> {
        TrackHistoryInteractorImpl(trackRepository = get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

    single<ExternalActionsInteractor> {
        ExternalActionsInteractorImpl(actions = get())
    }

    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(audioPlayerRepository = get())
    }
}