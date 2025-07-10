package com.practicum.playlistmaker.search.domain.api.repository

import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun trackSearch(text: String): Flow<Resource<List<Track>>>

    fun getHistory() : List<Track>

    fun updateHistory(tracks: List<Track>)

}