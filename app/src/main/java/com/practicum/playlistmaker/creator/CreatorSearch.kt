package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.TrackApi
import com.practicum.playlistmaker.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.interactor.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.repository.TrackRepository
import com.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CreatorSearch {

    private const val BASE_URL = "https://itunes.apple.com"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getApiService(): TrackApi {
        return getRetrofit().create(TrackApi::class.java)
    }

    private fun getTrackSearchRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(getApiService()))
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackSearchRepository())
    }
}