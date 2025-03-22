package com.practicum.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.App.Companion.TRACK_KEY
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import kotlinx.coroutines.Runnable
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var playTrack: ImageButton
    private var mediaPlayer = MediaPlayer()
    private lateinit var timerTW: TextView
    private var playerState = STATE_DEFAULT

    private var url: String? = ""

    private val updateRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                timerTW.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, 300)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playTrack = binding.btnPlayTrack
        timerTW = binding.trackTimePlayed

        val track: Track? = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                this.intent.getParcelableExtra(TRACK_KEY, Track::class.java)
            }
            else -> {
                this.intent.getParcelableExtra(TRACK_KEY)
            }
        }

        playTrack.setOnClickListener {
            playbackControl()
        }

        binding.apToolbar.setNavigationOnClickListener {
            finish()
        }

        val cornerRadius = (this.resources.getDimensionPixelSize(R.dimen.corner_radius_8))

        if (track != null) {
            Glide.with(this)
                .load(track.getCoverAtWork() ?: "")
                .transform(RoundedCorners(cornerRadius))
                .centerInside()
                .placeholder(R.drawable.ic_placeholder_cover)
                .error(R.drawable.ic_placeholder_cover)
                .into(binding.trackCover)

            url = track.previewUrl.toString()

            binding.trackNameAp.text = track.trackName ?: getString(R.string.nothing_found_message)
            binding.artistNameAp.text =
                track.artistName ?: getString(R.string.nothing_found_message)
            binding.tvRightDuration.text =
                track.trackTimeFormat() ?: getString(R.string.nothing_found_message)

            if (track.collectionName != null) {
                binding.tvRightAlbumName.text = track.collectionName
            } else {
                binding.tvRightAlbumName.isGone = true
                binding.tvLeftAlbumName.isGone = true
            }

            binding.tvRightTrackYear.text = track.getTrackYear()
            binding.tvRightTrackGenre.text = track.primaryGenreName
            binding.tvRightTrackCountry.text = track.country

            preparePlayer()

        } else {
            finish()
        }

        binding.apToolbar.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
        mediaPlayer.release()
    }

    private fun preparePlayer() {
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid track url", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playTrack.isEnabled = true
                playerState = STATE_PREPARED
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading track", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        mediaPlayer.setOnCompletionListener {
            playTrack.setImageResource(R.drawable.ic_play)
            timerTW.text = "00:00"
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playTrack.setImageResource(R.drawable.ic_pause)
        handler.post(updateRunnable)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playTrack.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}