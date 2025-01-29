package com.practicum.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {

    private const val BASE_URL = "https://itunes.apple.com"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val trackApi: TrackApi by lazy {
        retrofit.create(TrackApi::class.java)
    }
}