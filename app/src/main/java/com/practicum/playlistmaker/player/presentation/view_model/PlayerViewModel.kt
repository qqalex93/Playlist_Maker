package com.practicum.playlistmaker.player.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.history.domain.api.interactor.TrackHistoryInteractor
import com.practicum.playlistmaker.player.domain.api.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.player.presentation.mapper.PlayerPresenterTrackMapper
import com.practicum.playlistmaker.player.presentation.model.PlaybackState
import com.practicum.playlistmaker.player.presentation.model.PlayerState
import com.practicum.playlistmaker.player.presentation.model.PlayerTrackInfo
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val trackId: Int,
    private val playerInteractor: AudioPlayerInteractor,
    historyInteractor: TrackHistoryInteractor
) : ViewModel() {
    private val playerStateLiveData = MutableLiveData<PlayerState>()

    private val trackInfo: PlayerTrackInfo

    private var playerCurrentPosition: String = DEFAULT_CURRENT_POSITION

    private var timerJob: Job? = null

    init {
        val tracks = historyInteractor.getHistory()
        val track: Track? = getTrackFromHistory(tracks)
        trackInfo = PlayerPresenterTrackMapper.map(track)
        playerStateLiveData.value = PlayerState(
            isError = false,
            trackInfo = trackInfo,
            trackPlaybackState = PlaybackState.NOT_PREPARED,
            currentPosition = playerCurrentPosition
        )

        if (track != null) playerInteractor.playerPrepare(
            trackInfo.previewUrl,
            { preparedCallback() },
            { completionCallback() })
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while ((playerStateLiveData.value?.trackPlaybackState
                    ?: PlaybackState.NOT_PREPARED) == PlaybackState.PLAYING
            ) {
                delay(CURRENT_TRACK_TIME_DELAY)
                playerCurrentPosition = progressMap(playerInteractor.getCurrentPosition())
                playerStateLiveData.postValue(
                    PlayerState(
                    isError = false,
                    trackInfo = trackInfo,
                    trackPlaybackState = PlaybackState.PLAYING,
                    currentPosition = playerCurrentPosition))
            }
        }
    }

    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private fun getTrackFromHistory(tracks: List<Track>): Track? {
        tracks.forEach {
            if (it.trackId == trackId)
                return it
        }
        return null
    }

    private fun preparedCallback() {
        playerCurrentPosition = DEFAULT_CURRENT_POSITION
        playerStateLiveData.value = PlayerState(
            isError = false,
            trackInfo = trackInfo,
            trackPlaybackState = PlaybackState.PREPARED,
            currentPosition = playerCurrentPosition
        )
    }

    private fun completionCallback() {
        timerJob?.cancel()
        playerCurrentPosition = DEFAULT_CURRENT_POSITION
        playerStateLiveData.value = PlayerState(
            isError = false,
            trackInfo = trackInfo,
            trackPlaybackState = PlaybackState.PREPARED,
            currentPosition = playerCurrentPosition
        )
    }

    fun playerControl() {
        playerInteractor.playerControl(
            { playerStartCallback() },
            { playerPauseCallback() },
            { playerErrorCallback() }
        )
    }

    private fun playerStartCallback() {
        timerJob?.cancel()
        playerStateLiveData.value = PlayerState(
            isError = false,
            trackInfo = trackInfo,
            trackPlaybackState = PlaybackState.PLAYING,
            currentPosition = playerCurrentPosition
        )
        startTimer()
    }

    private fun playerPauseCallback() {
        timerJob?.cancel()
        playerStateLiveData.value = PlayerState(
            isError = false,
            trackInfo = trackInfo,
            trackPlaybackState = PlaybackState.PAUSED,
            currentPosition = playerCurrentPosition
        )
    }

    private fun playerErrorCallback() {
        timerJob?.cancel()
        playerCurrentPosition = DEFAULT_CURRENT_POSITION
        playerStateLiveData.value = PlayerState(
            isError = true,
            trackInfo = trackInfo,
            trackPlaybackState = PlaybackState.NOT_PREPARED,
            currentPosition = playerCurrentPosition
        )
    }

    fun playerPause() {
        if ((playerStateLiveData.value?.trackPlaybackState == PlaybackState.PLAYING)
        ) {
            playerInteractor.playerPause { playerPauseCallback() }
            playerStateLiveData.value = PlayerState(
                isError = false,
                trackInfo = trackInfo,
                trackPlaybackState = PlaybackState.PAUSED,
                currentPosition = playerCurrentPosition
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        timerJob = null
        playerInteractor.playerRelease()
    }

    private fun progressMap(progress: Int): String {
        return SimpleDateFormat(TRACK_TIME_VALUE, Locale.getDefault())
            .format(progress)
    }

    companion object {
        const val TRACK_TIME_VALUE = "mm:ss"
        const val CURRENT_TRACK_TIME_DELAY = 300L
        private const val DEFAULT_CURRENT_POSITION = "00:00"
    }
}