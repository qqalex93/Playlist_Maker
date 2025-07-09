package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.interactor.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.api.repository.TrackRepository
import com.practicum.playlistmaker.search.domain.models.ErrorType
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val trackRepository: TrackRepository) : TrackSearchInteractor {

    override fun trackSearch(text: String) : Flow<Pair<List<Track>?, ErrorType?>> {
        return trackRepository.trackSearch(text).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Empty -> {
                    Pair(listOf(), null)
                }

                is Resource.Error -> {
                    Pair(null, result.errorType)
                }

            }
        }
    }
}