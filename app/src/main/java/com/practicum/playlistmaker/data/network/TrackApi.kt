package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {
    @GET("/search?entity=song")
    fun trackSearch(@Query("term") text: String): Call<TrackSearchResponse>
}