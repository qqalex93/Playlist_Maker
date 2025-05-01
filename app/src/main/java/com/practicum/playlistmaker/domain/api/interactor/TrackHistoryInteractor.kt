package com.practicum.playlistmaker.domain.api.interactor

import com.practicum.playlistmaker.domain.models.Track

interface TrackHistoryInteractor {

    fun getHistory(): List<Track>

    fun updateHistory(newTrack: Track)

    fun clearHistory()

}