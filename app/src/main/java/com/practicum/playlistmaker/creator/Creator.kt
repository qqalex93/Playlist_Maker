package com.practicum.playlistmaker.creator

import android.app.Application
import android.media.MediaPlayer
import com.practicum.playlistmaker.data.audioPlayer.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.externalActions.ExternalActionsImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.TrackApi
import com.practicum.playlistmaker.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.data.themeSettings.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.api.interactor.ExternalActionsInteractor
import com.practicum.playlistmaker.domain.api.interactor.SettingsInteractor
import com.practicum.playlistmaker.domain.api.interactor.TrackHistoryInteractor
import com.practicum.playlistmaker.domain.api.interactor.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.repository.AudioPlayerRepository
import com.practicum.playlistmaker.domain.api.repository.ExternalActions
import com.practicum.playlistmaker.domain.api.repository.SettingsRepository
import com.practicum.playlistmaker.domain.api.repository.TrackRepository
import com.practicum.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.domain.impl.ExternalActionsInteractorImpl
import com.practicum.playlistmaker.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private lateinit var application: Application

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

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(getMediaPlayer())
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractorImpl {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getExternalActions(): ExternalActions {
        return ExternalActionsImpl(application)
    }

    fun provideExternalActionsInteractor(): ExternalActionsInteractor {
        return ExternalActionsInteractorImpl(getExternalActions())
    }

    private fun getTrackHistoryRepository(): TrackRepository {
        return TrackRepositoryImpl(application = application)
    }
    fun provideTrackInteractorHistory(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getTrackHistoryRepository())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }
}