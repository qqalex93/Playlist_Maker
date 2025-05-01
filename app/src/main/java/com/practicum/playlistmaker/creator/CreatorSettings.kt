package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.data.themeSettings.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.api.interactor.SettingsInteractor
import com.practicum.playlistmaker.domain.api.repository.SettingsRepository
import com.practicum.playlistmaker.domain.impl.SettingsInteractorImpl

object CreatorSettings {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }
}