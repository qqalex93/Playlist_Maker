package com.practicum.playlistmaker.search.data.dto

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    val trackId: Int?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Int?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)