package com.practicum.playlistmaker.media.presentation.models

sealed class PlaylistScreenState {
    data object Content: PlaylistScreenState()
    data object Empty: PlaylistScreenState()
}