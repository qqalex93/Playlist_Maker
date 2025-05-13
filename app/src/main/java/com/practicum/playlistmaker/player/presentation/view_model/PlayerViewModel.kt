package com.practicum.playlistmaker.player.presentation.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.history.domain.api.interactor.TrackHistoryInteractor
import com.practicum.playlistmaker.player.domain.api.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.player.presentation.mapper.PlayerPresenterTrackMapper
import com.practicum.playlistmaker.player.presentation.model.PlaybackState
import com.practicum.playlistmaker.player.presentation.model.PlayerState
import com.practicum.playlistmaker.player.presentation.model.PlayerTrackInfo
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val trackId: Int,
    private val playerInteractor: AudioPlayerInteractor,
    historyInteractor: TrackHistoryInteractor
) : ViewModel() {
    private val playerStateLiveData = MutableLiveData<PlayerState>()

    private val trackInfo: PlayerTrackInfo

    private val handler = Handler(Looper.getMainLooper())
    private var playerCurrentPosition: String = DEFAULT_CURRENT_POSITION

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

    private val getCurrentPosition = object : Runnable {
        override fun run() {
            playerCurrentPosition = progressMap(playerInteractor.getCurrentPosition())
            playerStateLiveData.postValue(
                PlayerState(
                    isError = false,
                    trackInfo = trackInfo,
                    trackPlaybackState = PlaybackState.PLAYING,
                    currentPosition = playerCurrentPosition
                )
            )
            handler.postDelayed(this, TRACK_TIME_DELAY)
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
        handler.removeCallbacks(getCurrentPosition)
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
        handler.removeCallbacks(getCurrentPosition)
        playerStateLiveData.value = PlayerState(
            isError = false,
            trackInfo = trackInfo,
            trackPlaybackState = PlaybackState.PLAYING,
            currentPosition = playerCurrentPosition
        )
        handler.post(getCurrentPosition)
    }

    private fun playerPauseCallback() {
        handler.removeCallbacks(getCurrentPosition)
        playerStateLiveData.value = PlayerState(
            isError = false,
            trackInfo = trackInfo,
            trackPlaybackState = PlaybackState.PAUSED,
            currentPosition = playerCurrentPosition
        )
    }

    private fun playerErrorCallback() {
        handler.removeCallbacks(getCurrentPosition)
        playerCurrentPosition = DEFAULT_CURRENT_POSITION
        playerStateLiveData.value = PlayerState(
            isError = true,
            trackInfo = trackInfo,
            trackPlaybackState = PlaybackState.NOT_PREPARED,
            currentPosition = playerCurrentPosition
        )
    }

    fun playerPause() {
        handler.removeCallbacks(getCurrentPosition)
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
        handler.removeCallbacks(getCurrentPosition)
        playerInteractor.playerRelease()
    }

    private fun progressMap(progress: Int): String {
        return SimpleDateFormat(TRACK_TIME_VALUE, Locale.getDefault())
            .format(progress)
    }

    companion object {
        const val TRACK_TIME_VALUE = "mm:ss"
        const val TRACK_TIME_DELAY = 300L
        private const val DEFAULT_CURRENT_POSITION = "00:00"
    }
}