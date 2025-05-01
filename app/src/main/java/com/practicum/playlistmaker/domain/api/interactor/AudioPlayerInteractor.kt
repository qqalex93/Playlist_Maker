package com.practicum.playlistmaker.domain.api.interactor

interface AudioPlayerInteractor {

    fun playerPrepare(
        resourceUrl: String, preparedCallback: () -> Unit,
        completionCallback: () -> Unit
    )

    fun playerControl(
        startCallback: () -> Unit,
        pauseCallback: () -> Unit,
        errorCallback: () -> Unit
    )

    fun playerStart(
        startCallback: () -> Unit,
        errorCallback: () -> Unit
    )

    fun playerPause(
        pauseCallback: () -> Unit,
        errorCallback: () -> Unit
    )

    fun playerRelease()

    fun getCurrentPosition(): String
}