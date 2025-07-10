package com.practicum.playlistmaker.search.domain.api.interactor

import com.practicum.playlistmaker.search.domain.models.ErrorType
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchInteractor {

    fun trackSearch(text: String): Flow<Pair<List<Track>?, ErrorType?>>
}
