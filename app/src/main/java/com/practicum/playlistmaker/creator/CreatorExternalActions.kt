package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.data.externalActions.ExternalActionsImpl
import com.practicum.playlistmaker.domain.api.interactor.ExternalActionsInteractor
import com.practicum.playlistmaker.domain.api.repository.ExternalActions
import com.practicum.playlistmaker.domain.impl.ExternalActionsInteractorImpl

object CreatorExternalActions {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getExternalActions(): ExternalActions {
        return ExternalActionsImpl(application)
    }

    fun provideExternalActionsInteractor(): ExternalActionsInteractor {
        return ExternalActionsInteractorImpl(getExternalActions())
    }
}