package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.interactor.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.api.repository.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Resource
import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val repository: TrackRepository) : TrackSearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun trackSearch(text: String, consumer: TrackSearchInteractor.TrackConsumer) {
        executor.execute {
            when (val resource = repository.trackSearch(text)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Empty -> {
                    consumer.consume(listOf(), null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.errorType)
                }

            }
        }
    }
}