package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.PlayerState
import com.practicum.playlistmaker.domain.api.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.repository.AudioPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerInteractorImpl(private val repository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun playerPrepare(
        resourceUrl: String, preparedCallback: () -> Unit, completionCallback: () -> Unit
    ) {
        repository.playerPrepare(resourceUrl, preparedCallback, completionCallback)
    }

    override fun playerControl(
        startCallback: () -> Unit, pauseCallback: () -> Unit, errorCallback: () -> Unit
    ) {
        val playerState = repository.getPlayerState()
        when (playerState) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                repository.playerStart()
                startCallback()
            }

            PlayerState.STATE_PLAYING -> {
                repository.playerPause()
                pauseCallback()
            }

            else -> errorCallback()
        }
    }

    override fun playerStart(startCallback: () -> Unit, errorCallback: () -> Unit) {
        val playerState = repository.getPlayerState()
        if (playerState != PlayerState.STATE_DEFAULT) {
            repository.playerStart()
            startCallback()
        }
    }

    override fun playerPause(pauseCallback: () -> Unit, errorCallback: () -> Unit) {
        val playerState = repository.getPlayerState()
        if (playerState != PlayerState.STATE_DEFAULT)
            repository.playerPause()
            pauseCallback()
    }

    override fun playerRelease() {
        repository.playerRelease()
    }

    override fun getCurrentPosition(): String {
        return SimpleDateFormat(
            TRACK_TIME,
            Locale.getDefault()
        ).format(repository.getCurrentPosition())
    }

    companion object {
        const val TRACK_TIME = "mm:ss"
    }
}