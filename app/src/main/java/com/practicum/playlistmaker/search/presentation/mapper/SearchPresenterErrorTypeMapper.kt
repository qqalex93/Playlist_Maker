package com.practicum.playlistmaker.search.presentation.mapper

import com.practicum.playlistmaker.search.domain.models.ErrorType
import com.practicum.playlistmaker.search.presentation.model.ErrorTypePresenter

object SearchPresenterErrorTypeMapper {
    fun map(errorType: ErrorType?): ErrorTypePresenter {
        if (errorType == null) return ErrorTypePresenter.BadRequest()

        return  when(errorType) {
            is ErrorType.NoConnection -> ErrorTypePresenter.NoConnection
            is ErrorType.BadRequest -> ErrorTypePresenter.BadRequest(errorType.message)
            is ErrorType.ErrorServer -> ErrorTypePresenter.ErrorServer(errorType.message)
            is ErrorType.EmptyResult -> ErrorTypePresenter.EmptyResult
        }
    }
}