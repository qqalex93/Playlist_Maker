package com.practicum.playlistmaker.presentation.ui.audioPlayer


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
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.presentation.ui.App.Companion.TRACK_KEY
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.Runnable



class AudioPlayerActivity : AppCompatActivity() {

    private val audioPlayerInteractor = Creator.provideAudioPlayerInteractor()

    private lateinit var binding: ActivityAudioPlayerBinding

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var playTrack: ImageButton
    private lateinit var timerTW: TextView

    private val updateRunnable = object : Runnable {
        override fun run() {
                timerTW.text = audioPlayerInteractor.getCurrentPosition()
                handler.postDelayed(this, REFRESH_DELAY_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playTrack = binding.btnPlayTrack
        playTrack.isEnabled = false

        val track: Track? = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                this.intent.getSerializableExtra(TRACK_KEY, Track::class.java)
            }
            else -> {
                this.intent.getSerializableExtra(TRACK_KEY) as Track
            }
        }

        playTrack.setOnClickListener {
            audioPlayerInteractor.playerControl(
                { startPlayerCallback() },
                { pausePlayerCallback() },
                { preparePlayerCallback() }
            )
        }

        binding.apToolbar.setNavigationOnClickListener {
            finish()
        }

        val cornerRadius = (this.resources.getDimensionPixelSize(R.dimen.corner_radius_8))

        if (track != null) {
            Glide.with(this)
                .load(track.artworkUrl512 ?: "")
                .transform(RoundedCorners(cornerRadius))
                .centerInside()
                .placeholder(R.drawable.ic_placeholder_cover)
                .error(R.drawable.ic_placeholder_cover)
                .into(binding.trackCover)

            if (track.previewUrl != null) {
                audioPlayerInteractor.playerPrepare(
                    track.previewUrl,
                    { preparePlayerCallback() },
                    { completionPlayerCallback() }
                )
            }

            binding.trackNameAp.text = track.trackName ?: getString(R.string.nothing_found_message)
            binding.artistNameAp.text =
                track.artistName ?: getString(R.string.nothing_found_message)
            binding.tvRightDuration.text =
                track.trackTimeMillis ?: getString(R.string.nothing_found_message)

            if (track.collectionName != null) {
                binding.tvRightAlbumName.text = track.collectionName
            } else {
                binding.tvRightAlbumName.isGone = true
                binding.tvLeftAlbumName.isGone = true
            }
            timerTW = findViewById(R.id.track_time_played)

            binding.tvRightTrackYear.text = track.releaseDate
            binding.tvRightTrackGenre.text = track.primaryGenreName
            binding.tvRightTrackCountry.text = track.country

        } else {
            finish()
        }

        binding.apToolbar.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.playerPause(
            { pausePlayerCallback() },
            { errorPlayerCallback() }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerInteractor.playerRelease()
    }

    private fun preparePlayerCallback() {
        playTrack.isEnabled = true
    }

    private fun completionPlayerCallback() {
        handler.removeCallbacks(updateRunnable)
        playTrack.setImageResource(R.drawable.ic_play)
        timerTW.text = "00:00"
    }

    private fun errorPlayerCallback() {
        Toast.makeText(this, "Error loading track", Toast.LENGTH_SHORT).show()
    }

    private fun startPlayerCallback() {
        handler.post(updateRunnable)
        playTrack.setImageResource(R.drawable.ic_pause)
    }

    private fun pausePlayerCallback() {
        handler.removeCallbacks(updateRunnable)
        playTrack.setImageResource(R.drawable.ic_play)
    }

    companion object {

        private const val REFRESH_DELAY_MS = 300L
    }
}