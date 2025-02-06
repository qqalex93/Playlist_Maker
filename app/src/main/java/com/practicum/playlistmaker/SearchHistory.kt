package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun getTrackFromHistory(): List<Track> = readTrackListFromSP()

    private val gson = Gson()

    fun saveTrack(track: Track) {
        val tracks = readTrackListFromSP().toMutableList()
        val index = theSameTracks(tracks, track)

        if (index != -1) {
            tracks.removeAt(index)
        }

        tracks.add(0, track)
        if (tracks.size > MAX_COUNT_TRACKS) {
            tracks.retainAll(tracks.take(MAX_COUNT_TRACKS))
        }

        writeTrackListToSP(tracks)
    }

    private fun writeTrackListToSP(tracks: List<Track>) {
        sharedPreferences.edit()
            .putString(KEY_HISTORY_TRACK_LIST, gson.toJson(tracks))
            .apply()
    }

    private fun readTrackListFromSP(): List<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY_TRACK_LIST, null) ?: return emptyList()
            val type: Type = object : TypeToken<List<Track>>() {}.type
            return gson.fromJson(json, type) ?: emptyList()
    }

    fun clearSearchHistory() {
        writeTrackListToSP(emptyList())
    }

    private fun theSameTracks(tracks: List<Track>, track: Track): Int {
        return tracks.indexOfFirst { element ->
            (element.trackId != null && element.trackId == track.trackId) || (element.trackId == null && element == track)
        }
    }

    private companion object {
        const val MAX_COUNT_TRACKS = 10
        const val KEY_HISTORY_TRACK_LIST = "history_track_list"
    }
}