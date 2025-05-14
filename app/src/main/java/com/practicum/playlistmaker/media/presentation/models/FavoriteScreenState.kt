package com.practicum.playlistmaker.media.presentation.models

sealed class FavoriteScreenState {
    data object Content: FavoriteScreenState()
    data object Empty: FavoriteScreenState()
}