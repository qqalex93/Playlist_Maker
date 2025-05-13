package com.practicum.playlistmaker.history.domain.api.interactor

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackHistoryInteractor {

    fun getHistory(): List<Track>

    fun updateHistory(newTrack: Track)

    fun clearHistory()

}