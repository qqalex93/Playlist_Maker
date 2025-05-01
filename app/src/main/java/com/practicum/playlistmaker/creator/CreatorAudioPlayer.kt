package com.practicum.playlistmaker.creator

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.audioPlayer.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.domain.api.repository.AudioPlayerRepository
import com.practicum.playlistmaker.domain.impl.AudioPlayerInteractorImpl

object CreatorAudioPlayer {

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(getMediaPlayer())
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractorImpl {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }
}