package com.practicum.playlistmaker.domain.models

import java.io.Serializable

data class Track(
    val trackId: Int?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val artworkUrl512: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
) : Serializable
