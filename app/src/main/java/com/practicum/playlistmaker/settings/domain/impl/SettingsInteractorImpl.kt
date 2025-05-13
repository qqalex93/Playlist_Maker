package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.interactor.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.repository.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getTheme(): Boolean {
        return repository.getTheme()
    }

    override fun setAndSaveTheme(isDarkThemeOn: Boolean) {
        repository.saveTheme(isDarkThemeOn)
        repository.setSavedTheme()
    }

    override fun setSavedTheme() {
        repository.setSavedTheme()
    }
}