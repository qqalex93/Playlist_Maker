package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.interactor.TrackHistoryInteractor
import com.practicum.playlistmaker.domain.api.repository.TrackRepository
import com.practicum.playlistmaker.domain.impl.TrackHistoryInteractorImpl

object CreatorHistory {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getTrackHistoryRepository(): TrackRepository {
        return TrackRepositoryImpl(application = application)
    }
    fun provideTrackInteractorHistory(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getTrackHistoryRepository())
    }
}