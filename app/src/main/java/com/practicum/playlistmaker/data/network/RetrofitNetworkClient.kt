package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.NetworkResponse
import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import java.io.IOException
import com.practicum.playlistmaker.presentation.ui.App.Companion.BAD_REQUEST_CODE
import com.practicum.playlistmaker.presentation.ui.App.Companion.ERROR_SERVER_CODE

class RetrofitNetworkClient(private val trackApiService: TrackApi) : NetworkClient {

    override fun doRequest(dto: Any): NetworkResponse {
        if (dto is TrackSearchRequest) {
            val response = try {
                trackApiService.trackSearch(dto.text).execute()
            } catch (e: IOException) {
                null
            }

            if (response == null) {
                return NetworkResponse().apply { resultCode = ERROR_SERVER_CODE }
            }

            val body = response.body() ?: NetworkResponse()

            return body.apply { resultCode = response.code() }
        } else {
            return NetworkResponse().apply { resultCode = BAD_REQUEST_CODE }
        }
    }
}