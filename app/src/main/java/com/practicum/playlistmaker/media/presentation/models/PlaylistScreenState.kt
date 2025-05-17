package com.practicum.playlistmaker.media.presentation.models

sealed class PlaylistsScreenState {
    data object Content: PlaylistsScreenState()
    data object Empty: PlaylistsScreenState()
}