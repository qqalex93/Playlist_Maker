package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.interactor.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.repository.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getTheme(): Boolean {
        return settingsRepository.getTheme()
    }

    override fun setAndSaveTheme(isDarkThemeOn: Boolean) {
        settingsRepository.saveTheme(isDarkThemeOn)
        settingsRepository.setSavedTheme()
    }

    override fun setSavedTheme() {
        settingsRepository.setSavedTheme()
    }
}