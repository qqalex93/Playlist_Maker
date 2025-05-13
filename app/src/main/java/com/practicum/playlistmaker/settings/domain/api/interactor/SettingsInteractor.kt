package com.practicum.playlistmaker.settings.domain.api.interactor

interface SettingsInteractor {

    fun getTheme(): Boolean

    fun setAndSaveTheme(isDarkThemeOn: Boolean)

    fun setSavedTheme()

}