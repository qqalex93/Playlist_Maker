package com.practicum.playlistmaker.media.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.presentation.models.FavoriteScreenState

class FavoritesFragmentViewModel() : ViewModel() {
    private val screenStateLiveData: MutableLiveData<FavoriteScreenState> =
        MutableLiveData(FavoriteScreenState.Empty)

    fun screenStateObserve(): LiveData<FavoriteScreenState> = screenStateLiveData
}