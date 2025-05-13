package com.practicum.playlistmaker.player.presentation.model

data class PlayerTrackInfo(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl: String,
    val collectionName: String?,
    val releaseDate: String,
    val genre: String,
    val country: String,
    val previewUrl: String
) {
    companion object {
        fun empty(): PlayerTrackInfo {
            return PlayerTrackInfo(
                trackId = UNKNOWN_ID,
                trackName = EMPTY_STRING,
                artistName = EMPTY_STRING,
                trackTime = EMPTY_STRING,
                artworkUrl = EMPTY_LINK,
                collectionName = null,
                releaseDate = EMPTY_STRING,
                genre = EMPTY_STRING,
                country = EMPTY_STRING,
                previewUrl = EMPTY_LINK
            )
        }

        private const val UNKNOWN_ID = -1
        private const val EMPTY_STRING = "no result"
        private const val EMPTY_LINK = ""
    }
}