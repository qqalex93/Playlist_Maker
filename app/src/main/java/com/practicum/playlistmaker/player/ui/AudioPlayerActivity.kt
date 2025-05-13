package com.practicum.playlistmaker.player.ui


import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.app.App.Companion.TRACK_KEY
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.player.presentation.model.PlaybackState
import com.practicum.playlistmaker.player.presentation.model.PlayerTrackInfo
import com.practicum.playlistmaker.player.presentation.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Runnable


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackId: Int = this.intent.getIntExtra(TRACK_KEY, -1)

        binding.apToolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(trackId)
        )[PlayerViewModel::class.java]

        viewModel.getPlayerStateLiveData().observe(this) { playerState ->
            if (playerState.isError) showPlayerError(
                playerState.trackInfo,
                playerState.currentPosition
            )
            else {
                when (playerState.trackPlaybackState) {
                    PlaybackState.NOT_PREPARED -> {
                        showNotPreparedPlayer(playerState.trackInfo, playerState.currentPosition)
                    }

                    PlaybackState.PREPARED -> {
                        showPreparedPlayer(playerState.trackInfo, playerState.currentPosition)
                    }

                    PlaybackState.PLAYING -> {
                        showPlayingPlayer(playerState.currentPosition)
                    }

                    PlaybackState.PAUSED -> {
                        showPausedPlayer(playerState.trackInfo, playerState.currentPosition)
                    }
                }
            }
        }

        binding.btnPlayTrack.setOnClickListener {
            viewModel.playerControl()
        }
    }

    private fun setTrackContent(trackInfo: PlayerTrackInfo) {

        val cornerRadius = (this.resources.getDimensionPixelSize(R.dimen.corner_radius_8))

        Glide.with(this)
            .load(trackInfo.artworkUrl)
            .transform(RoundedCorners(cornerRadius))
            .centerInside()
            .placeholder(R.drawable.ic_placeholder_cover)
            .error(R.drawable.ic_placeholder_cover)
            .into(binding.trackCover)

        binding.trackNameAp.text = trackInfo.trackName
        binding.artistNameAp.text = trackInfo.artistName
        binding.tvRightDuration.text = trackInfo.trackTime

        if (trackInfo.collectionName != null) {
            binding.tvRightAlbumName.text = trackInfo.collectionName
            binding.tvRightAlbumName.isGone = false
            binding.tvLeftAlbumName.isGone = false
        } else {
            binding.tvRightAlbumName.isGone = true
            binding.tvLeftAlbumName.isGone = true
        }
        binding.trackTimePlayed.text = getString(R.string.current_track_time_placeholder)
        binding.tvRightTrackYear.text = trackInfo.releaseDate
        binding.tvRightTrackGenre.text = trackInfo.genre
        binding.tvRightTrackCountry.text = trackInfo.country
    }


    private fun showNotPreparedPlayer(trackInfo: PlayerTrackInfo, currentPlayerPosition: String) {
        setTrackContent(trackInfo)
        binding.btnPlayTrack.isEnabled = false
        binding.btnPlayTrack.setImageResource(R.drawable.ic_play)
        binding.trackTimePlayed.text = currentPlayerPosition
    }


    private fun showPreparedPlayer(trackInfo: PlayerTrackInfo, currentPlayerPosition: String) {
        setTrackContent(trackInfo)
        binding.btnPlayTrack.isEnabled = true
        binding.btnPlayTrack.setImageResource(R.drawable.ic_play)
        binding.trackTimePlayed.text = currentPlayerPosition
    }

    private fun showPlayingPlayer(currentPlayerPosition: String) {
        binding.btnPlayTrack.setImageResource(R.drawable.ic_pause)
        binding.trackTimePlayed.text = currentPlayerPosition
    }

    private fun showPausedPlayer(trackInfo: PlayerTrackInfo, currentPlayerPosition: String) {
        setTrackContent(trackInfo)
        binding.btnPlayTrack.setImageResource(R.drawable.ic_play)
        binding.trackTimePlayed.text = currentPlayerPosition
    }

    private fun showPlayerError(trackInfo: PlayerTrackInfo, currentPlayerPosition: String) {
        setTrackContent(trackInfo)
        binding.btnPlayTrack.setImageResource(R.drawable.ic_play)
        binding.trackTimePlayed.text = currentPlayerPosition
        Toast.makeText(
            this,
            getString(R.string.something_wrong_message), Toast.LENGTH_LONG
        ).show()
    }

    override fun onPause() {
        super.onPause()
        viewModel.playerPause()
    }
}