package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.api.TrackApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.practicum.playlistmaker.app.App.Companion.DI_HISTORY_SHARED_PREFERENCES
import com.practicum.playlistmaker.app.App.Companion.DI_SETTINGS_SHARED_PREFERENCES
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.impl.RetrofitNetworkClient
import org.koin.android.ext.koin.androidApplication
import retrofit2.create

private const val BASE_URL = "https://itunes.apple.com"
private const val HISTORY_SHARED_PREFERENCES = "shared_preferences_history"
private const val SETTINGS_SHARED_PREFERENCES = "shared_preferences_settings"

val dataModule = module {
    single<TrackApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApi::class.java)
    }

    single(named(DI_HISTORY_SHARED_PREFERENCES)) {
        androidApplication().getSharedPreferences(
            HISTORY_SHARED_PREFERENCES, Context.MODE_PRIVATE
        )
    }

    single(named(DI_SETTINGS_SHARED_PREFERENCES)) {
        androidApplication().getSharedPreferences(
            SETTINGS_SHARED_PREFERENCES, Context.MODE_PRIVATE
        )
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidApplication())
    }

    factory {
        Gson()
    }

    factory {
        MediaPlayer()
    }
}
