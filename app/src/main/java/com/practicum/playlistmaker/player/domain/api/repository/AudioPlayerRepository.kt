package com.practicum.playlistmaker.player.domain.api.repository

import com.practicum.playlistmaker.player.domain.models.PlayerState

interface AudioPlayerRepository {
    fun playerPrepare(resourceUrl: String, preparedCallback: () -> Unit,
                      completionCallback: () -> Unit)

    fun getPlayerState() : PlayerState

    fun getCurrentPosition() : Int

    fun playerStart()

    fun playerPause()

    fun playerStop()

    fun playerRelease()
}