package com.practicum.playlistmaker.history.domain.impl

import com.practicum.playlistmaker.history.domain.api.interactor.TrackHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.repository.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track

class TrackHistoryInteractorImpl(private val trackRepository: TrackRepository) : TrackHistoryInteractor {

    override fun getHistory(): List<Track> {
        return trackRepository.getHistory()
    }

    override fun updateHistory(newTrack: Track) {
        var tracks: MutableList<Track> = getHistory().toMutableList()

        val index = theSameTracks(tracks, newTrack)
        if (index != -1) {
            tracks.remove(newTrack)
        }

        tracks.add(0, newTrack)
        if (tracks.size > MAX_COUNT_TRACKS)
            tracks = tracks.take(MAX_COUNT_TRACKS).toMutableList()

        trackRepository.updateHistory(tracks)
    }

    override fun clearHistory() {
        trackRepository.updateHistory(listOf())
    }

    private fun theSameTracks(tracks: List<Track>, track: Track): Int {
        tracks.forEachIndexed { index, element ->
            if (element.trackId != null && track.trackId != null) {
                if (element.trackId == track.trackId)
                    return index
            } else if (element == track)
                return index
        }
        return -1
    }

    companion object {
        const val MAX_COUNT_TRACKS = 10
    }
}