package com.practicum.playlistmaker.domain.api.interactor

import com.practicum.playlistmaker.domain.models.Track

interface TrackSearchInteractor {

    fun trackSearch(text: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?)
    }
}