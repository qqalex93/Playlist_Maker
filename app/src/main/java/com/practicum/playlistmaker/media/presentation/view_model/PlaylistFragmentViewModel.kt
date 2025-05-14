package com.practicum.playlistmaker.media.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.presentation.models.PlaylistScreenState

class PlaylistFragmentViewModel() : ViewModel() {
    private val screenStateLiveData: MutableLiveData<PlaylistScreenState> =
        MutableLiveData(PlaylistScreenState.Empty)

    fun screenStateObserve(): LiveData<PlaylistScreenState> = screenStateLiveData
}