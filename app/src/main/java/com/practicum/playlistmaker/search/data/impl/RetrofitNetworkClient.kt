package com.practicum.playlistmaker.search.data.impl

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.NetworkResponse
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import java.io.IOException
import com.practicum.playlistmaker.search.data.api.TrackApi
import com.practicum.playlistmaker.search.data.dto.NetworkResponseCode

class RetrofitNetworkClient(
    private val trackApiService: TrackApi,
    private val application: Application
) : NetworkClient {

    override fun doRequest(dto: Any): NetworkResponse {
        if (!isConnected()) {
            return NetworkResponse().apply {
                resultCode = NetworkResponseCode.NO_CONNECTION
            }
        }

        if (dto is TrackSearchRequest) {
            val response = try {
                trackApiService.trackSearch(dto.text).execute()
            } catch (e: IOException) {
                null
            }

            if (response == null) {
                return NetworkResponse().apply {
                    resultCode = NetworkResponseCode.ERROR_SERVER
                }
            }

            if (response.body() == null) return NetworkResponse().apply {
                resultCode = NetworkResponseCode.BAD_REQUEST
            }

            val body = response.body() ?: NetworkResponse()

            return body.apply {
                resultCode = NetworkResponseCode.SUCCESS
            }
        } else {
            return NetworkResponse().apply {
                resultCode = NetworkResponseCode.BAD_REQUEST
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val capabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true

            }
        }
        return false
    }
}