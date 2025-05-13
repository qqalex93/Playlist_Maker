package com.practicum.playlistmaker.search.presentation.model

data class SearchTrackInfo(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val artworkUrl: String,
    val trackTime: String
)
