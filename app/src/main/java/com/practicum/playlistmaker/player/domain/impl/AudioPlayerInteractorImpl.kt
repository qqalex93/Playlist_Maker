package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.models.PlayerState
import com.practicum.playlistmaker.player.domain.api.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.api.repository.AudioPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerInteractorImpl(private val repository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun playerPrepare(
        resourceUrl: String,
        preparedCallback: () -> Unit,
        completionCallback: () -> Unit
    ) {
        repository.playerPrepare(resourceUrl, preparedCallback, completionCallback)
    }

    override fun playerControl(
        startCallback: () -> Unit,
        pauseCallback: () -> Unit,
        errorCallback: () -> Unit
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

    override fun playerStart(startCallback: () -> Unit) {
        val playerState = repository.getPlayerState()
        if (playerState != PlayerState.STATE_DEFAULT) {
            repository.playerStart()
            startCallback()
        }
    }

    override fun playerPause(pauseCallback: () -> Unit) {
        val playerState = repository.getPlayerState()
        if (playerState != PlayerState.STATE_DEFAULT)
            repository.playerPause()
            pauseCallback()
    }

    override fun playerRelease() {
        repository.playerRelease()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

}