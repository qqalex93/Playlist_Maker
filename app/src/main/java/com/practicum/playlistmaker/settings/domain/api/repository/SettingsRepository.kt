package com.practicum.playlistmaker.settings.domain.api.repository

interface SettingsRepository {

    fun getTheme(): Boolean

    fun saveTheme(isDarkModeOn: Boolean)

    fun setSavedTheme()

}