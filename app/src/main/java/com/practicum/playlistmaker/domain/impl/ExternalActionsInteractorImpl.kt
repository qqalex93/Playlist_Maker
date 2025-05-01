package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.interactor.ExternalActionsInteractor
import com.practicum.playlistmaker.domain.api.repository.ExternalActions

class ExternalActionsInteractorImpl(private val actions: ExternalActions) : ExternalActionsInteractor {

    override fun shareApp(link: String) {
        actions.shareApp(link)
    }

    override fun writeToSupport(targetMail: String, subjectMail: String, messageMail: String) {
        actions.writeToSupport(targetMail, subjectMail, messageMail)
    }

    override fun userAgreement(url: String) {
        actions.userAgreement(url)
    }
}