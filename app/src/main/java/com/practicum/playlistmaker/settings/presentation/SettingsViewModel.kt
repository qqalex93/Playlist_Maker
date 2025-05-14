package com.practicum.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.api.interactor.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.api.interactor.ExternalActionsInteractor
import com.practicum.playlistmaker.support.SingleEventLiveData

class SettingsViewModel(
    private val externalActionsInteractor: ExternalActionsInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val isDefaultThemeDarkLiveData = SingleEventLiveData<Boolean>()

    init {
        isDefaultThemeDarkLiveData.value = settingsInteractor.getTheme()
    }

    fun getIsDefaultThemeDarkLiveData(): LiveData<Boolean> = isDefaultThemeDarkLiveData

    fun changeTheme(isDarkThemeOn: Boolean) {
        settingsInteractor.setAndSaveTheme(isDarkThemeOn)
    }

    fun shareApp(shareAppLink: String) {
        externalActionsInteractor.shareApp(shareAppLink)
    }

    fun writeToSupport(targetMail: String, themeMessage: String, messageContent: String) {
        externalActionsInteractor.writeToSupport(targetMail, themeMessage, messageContent)
    }

    fun userAgreement(url: String) {
        externalActionsInteractor.userAgreement(url)
    }
}