package com.practicum.playlistmaker.domain.api.repository

interface ExternalActions {

    fun shareApp(link: String)

    fun writeToSupport(targetMail: String, themeMessage: String, messageContent: String)

    fun userAgreement(url: String)
}