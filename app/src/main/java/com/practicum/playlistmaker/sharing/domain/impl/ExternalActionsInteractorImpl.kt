package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.interactor.ExternalActionsInteractor
import com.practicum.playlistmaker.sharing.domain.api.repository.ExternalActions

class ExternalActionsInteractorImpl(private val actions: ExternalActions) :
    ExternalActionsInteractor {

    override fun shareApp(link: String) {
        actions.shareApp(link)
    }

    override fun writeToSupport(targetMail: String, themeMessage: String, messageContent: String) {
        actions.writeToSupport(targetMail, themeMessage, messageContent)
    }

    override fun userAgreement(url: String) {
        actions.userAgreement(url)
    }
}