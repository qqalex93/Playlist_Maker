package com.practicum.playlistmaker.search.domain.api.interactor

import com.practicum.playlistmaker.search.domain.models.ErrorType
import com.practicum.playlistmaker.search.domain.models.Track

interface TrackSearchInteractor {

    fun trackSearch(text: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?, errorType: ErrorType?)
    }
}