package com.practicum.playlistmaker.data.network

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.domain.api.repository.TrackRepository
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.ui.App.Companion.SUCCESS_CODE
import java.lang.reflect.Type
import androidx.core.content.edit
import com.practicum.playlistmaker.data.NetworkConst

class TrackRepositoryImpl(
    private val networkClient: NetworkClient? = null,
    private val application: Application? = null
) : TrackRepository {

    private val sharedPreferences: SharedPreferences? = application?.getSharedPreferences(
        NetworkConst.SHARED_PREFERENCES, Context.MODE_PRIVATE)

    override fun trackSearch(text: String): List<Track>? {
        if (networkClient != null) {
            val response = networkClient.doRequest(TrackSearchRequest(text))
            return if (response.resultCode == SUCCESS_CODE) {
                (response as TrackSearchResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeFormat(),
                        it.artworkUrl100,
                        it.getCoverAtWork(),
                        it.collectionName,
                        it.getTrackYear(),
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                }
            } else {
                null
            }
        } else return null
    }

    override fun getHistory(): List<Track> {
        if (sharedPreferences != null) {
            val json = sharedPreferences.getString(NetworkConst.KEY_FOR_HISTORY_TRACK_LIST, null)
            return if (json != null) {
                val type: Type = object : TypeToken<List<Track>>() {}.type
                Gson().fromJson(json, type) ?: listOf()
            } else {
                listOf()
            }
        } else return listOf()
    }

    override fun updateHistory(tracks: List<Track>) {
        if (sharedPreferences != null) {
            val json: String = Gson().toJson(tracks)
            sharedPreferences.edit() {
                putString(NetworkConst.KEY_FOR_HISTORY_TRACK_LIST, json)
            }
        }
    }

}