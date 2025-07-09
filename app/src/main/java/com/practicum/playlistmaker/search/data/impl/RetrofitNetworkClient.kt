package com.practicum.playlistmaker.search.data.impl

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.NetworkResponse
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.api.TrackApi
import com.practicum.playlistmaker.search.data.dto.NetworkResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val trackApiService: TrackApi,
    private val application: Application
) : NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResponse {
        if (!isConnected()) {
            return NetworkResponse().apply {
                resultCode = NetworkResponseCode.NO_CONNECTION
            }
        }

        if (dto !is TrackSearchRequest) {
            return NetworkResponse().apply {
                resultCode = NetworkResponseCode.BAD_REQUEST
            }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = trackApiService.trackSearch(dto.text)
                response.apply {
                    resultCode = NetworkResponseCode.SUCCESS
                }
            } catch (e: Throwable) {
                NetworkResponse().apply {
                    resultCode = NetworkResponseCode.ERROR_SERVER
                }
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