package com.practicum.playlistmaker.search.data.dto

enum class NetworkResponseCode(private val code: Int) {
    NO_CONNECTION(-1),
    BAD_REQUEST(400),
    ERROR_SERVER(500),
    SUCCESS(200)

}