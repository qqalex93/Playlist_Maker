package com.practicum.playlistmaker.sharing.domain.models

data class EmailData(
    val targetMail: String,
    val themeMessage: String,
    val messageContent: String
)