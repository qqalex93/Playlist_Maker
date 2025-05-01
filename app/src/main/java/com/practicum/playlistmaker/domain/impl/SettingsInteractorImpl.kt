package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.interactor.SettingsInteractor
import com.practicum.playlistmaker.domain.api.repository.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getTheme(): Boolean {
        return repository.getTheme()
    }

    override fun saveTheme(isDarkModeOn: Boolean) {
        repository.savedTheme(isDarkModeOn)
    }
}