package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.presentation.view_model.FavoritesFragmentViewModel
import com.practicum.playlistmaker.media.presentation.view_model.PlaylistFragmentViewModel
import com.practicum.playlistmaker.player.presentation.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.presentation.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModeModel = module {
    viewModel {
        SearchViewModel(trackSearchInteractor = get(), trackHistoryInteractor = get())
    }

    viewModel {
        SettingsViewModel(externalActionsInteractor = get(), settingsInteractor = get())
    }

    viewModel { (trackId: Int) ->
        PlayerViewModel(
            trackId = trackId,
            playerInteractor = get(),
            historyInteractor = get()
        )
    }
    viewModel {
        FavoritesFragmentViewModel()
    }

    viewModel {
        PlaylistFragmentViewModel()
    }
}