package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.interactor.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.repository.TrackRepository
import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val repository: TrackRepository) : TrackSearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun trackSearch(text: String, consumer: TrackSearchInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.trackSearch(text))
        }
    }
}