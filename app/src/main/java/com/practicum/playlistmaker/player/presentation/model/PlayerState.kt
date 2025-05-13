package com.practicum.playlistmaker.player.presentation.model

data class PlayerState(
    val isError: Boolean = false,
    val trackInfo: PlayerTrackInfo = PlayerTrackInfo.empty(),
    val trackPlaybackState: PlaybackState = PlaybackState.NOT_PREPARED,
    val currentPosition: String = DEFAULT_CURRENT_POSITION
) {
    companion object {
        private const val DEFAULT_CURRENT_POSITION = "00:00"
    }
}