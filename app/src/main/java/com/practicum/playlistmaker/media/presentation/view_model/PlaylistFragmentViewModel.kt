package com.practicum.playlistmaker.media.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.presentation.models.PlaylistsScreenState

class PlaylistsFragmentViewModel() : ViewModel() {
    private val screenStateLiveData: MutableLiveData<PlaylistsScreenState> =
        MutableLiveData(PlaylistsScreenState.Empty)

    fun screenStateObserve(): LiveData<PlaylistsScreenState> = screenStateLiveData
}