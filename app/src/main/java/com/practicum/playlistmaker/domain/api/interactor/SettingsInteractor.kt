package com.practicum.playlistmaker.domain.api.interactor

interface SettingsInteractor {

    fun getTheme(): Boolean

    fun saveTheme(isDarkModeOn: Boolean)

}