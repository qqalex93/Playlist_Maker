package com.practicum.playlistmaker.search.presentation.model

sealed class ErrorTypePresenter(message: String? = null) {
    data object NoConnection : ErrorTypePresenter()
    data class BadRequest(val message: String? = null) : ErrorTypePresenter(message)
    data class ErrorServer(val message: String? = null) : ErrorTypePresenter(message)
    data object EmptyResult : ErrorTypePresenter()
}