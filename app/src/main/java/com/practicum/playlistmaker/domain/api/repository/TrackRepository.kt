package com.practicum.playlistmaker.domain.api.repository

import com.practicum.playlistmaker.domain.models.Track

interface TrackRepository {

    fun trackSearch(text: String): List<Track>?

    fun getHistory() : List<Track>

    fun updateHistory(tracks: List<Track>)

}