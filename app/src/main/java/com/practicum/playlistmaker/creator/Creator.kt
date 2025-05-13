package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.sharing.data.ExternalActionsImpl
import com.practicum.playlistmaker.search.data.impl.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.api.TrackApi
import com.practicum.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.api.interactor.ExternalActionsInteractor
import com.practicum.playlistmaker.settings.domain.api.interactor.SettingsInteractor
import com.practicum.playlistmaker.history.domain.api.interactor.TrackHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.interactor.TrackSearchInteractor
import com.practicum.playlistmaker.player.domain.api.repository.AudioPlayerRepository
import com.practicum.playlistmaker.sharing.domain.api.repository.ExternalActions
import com.practicum.playlistmaker.settings.domain.api.repository.SettingsRepository
import com.practicum.playlistmaker.search.domain.api.repository.TrackRepository
import com.practicum.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.sharing.domain.impl.ExternalActionsInteractorImpl
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.history.domain.impl.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TrackSearchInteractorImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates

object Creator {

    private lateinit var application: Application
    private var isDefaultThemeDark by Delegates.notNull<Boolean>()

    private const val BASE_URL = "https://itunes.apple.com"

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getSharedPreference(instanceName: String): SharedPreferences {
        return application.getSharedPreferences(
            instanceName, Context.MODE_PRIVATE
        )
    }

    private fun getApiService(): TrackApi {
        return getRetrofit().create(TrackApi::class.java)
    }

    private fun getTrackSearchRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(getApiService(), application))
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

    private fun getExternalActions(): ExternalActions {
        return ExternalActionsImpl(application)
    }

    fun provideExternalActionsInteractor(): ExternalActionsInteractor {
        return ExternalActionsInteractorImpl(getExternalActions())
    }

    private fun getTrackHistoryRepository(): TrackRepository {
        return TrackRepositoryImpl(sharedPreferences = getSharedPreference(HISTORY_SHARED_PREFERENCES))
    }
    fun provideTrackInteractorHistory(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getTrackHistoryRepository())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(isDefaultThemeDark, getSharedPreference(SETTINGS_SHARED_PREFERENCES))
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun initDefaultTheme(isDefaultThemeDark: Boolean) {
        this.isDefaultThemeDark = isDefaultThemeDark
    }


    private const val HISTORY_SHARED_PREFERENCES = "history_shared_preferences"
    private const val SETTINGS_SHARED_PREFERENCES = "settings_shared_preferences"
}