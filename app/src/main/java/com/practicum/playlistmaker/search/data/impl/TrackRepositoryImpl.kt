package com.practicum.playlistmaker.search.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.repository.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import java.lang.reflect.Type
import androidx.core.content.edit
import com.practicum.playlistmaker.search.data.dto.NetworkResponseCode
import com.practicum.playlistmaker.search.data.mapper.SearchRepositoryTrackMapper
import com.practicum.playlistmaker.search.domain.models.ErrorType
import com.practicum.playlistmaker.search.domain.models.Resource

class TrackRepositoryImpl(
    private val networkClient: NetworkClient? = null,
    private val sharedPreferences: SharedPreferences? = null
) : TrackRepository {

    override fun trackSearch(text: String): Resource<List<Track>> {
        if (networkClient != null) {
            val response = networkClient.doRequest(TrackSearchRequest(text))

            when (response.resultCode) {
                NetworkResponseCode.SUCCESS -> {
                    val result = (response as TrackSearchResponse).results
                    return if (result.isEmpty()) Resource.Empty()
                    else Resource.Success(result.map { SearchRepositoryTrackMapper.map(it) })
                }

                NetworkResponseCode.NO_CONNECTION -> {
                    return Resource.Error(errorType = ErrorType.NoConnection)
                }

                NetworkResponseCode.BAD_REQUEST -> {
                    return Resource.Error(errorType = ErrorType.BadRequest())
                }

                NetworkResponseCode.ERROR_SERVER -> {
                    return Resource.Error(errorType = ErrorType.ErrorServer())
                }
            }
        }
        return Resource.Error(ErrorType.BadRequest())
    }

    override fun getHistory(): List<Track> {
        if (sharedPreferences != null) {
            val json =
                sharedPreferences.getString(KEY_HISTORY_TRACK_LIST, null)
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
                putString(KEY_HISTORY_TRACK_LIST, json)
            }
        }
    }
    companion object {
        private const val KEY_HISTORY_TRACK_LIST = "history_track_list"
    }
}