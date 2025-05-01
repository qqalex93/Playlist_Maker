package com.practicum.playlistmaker.domain.api.repository

interface SettingsRepository {

    fun getTheme(): Boolean

    fun savedTheme(isDarkModeOn: Boolean)

}