package com.practicum.playlistmaker.search.domain.models

sealed class ErrorType(message: String? = null) {
    data object NoConnection : ErrorType()
    data class BadRequest(val message: String? = null) : ErrorType(message)
    data class ErrorServer(val message: String? = null) : ErrorType(message)
    data object EmptyResult : ErrorType()
}