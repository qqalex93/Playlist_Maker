package com.practicum.playlistmaker.search.domain.api.repository

import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track

interface TrackRepository {

    fun trackSearch(text: String): Resource<List<Track>>

    fun getHistory() : List<Track>

    fun updateHistory(tracks: List<Track>)

}