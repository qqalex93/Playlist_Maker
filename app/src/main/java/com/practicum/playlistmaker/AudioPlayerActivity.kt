package com.practicum.playlistmaker

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.App.Companion.TRACK_KEY
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track: Track? = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                this.intent.getParcelableExtra(TRACK_KEY, Track::class.java)
            }
            else -> {
                this.intent.getParcelableExtra(TRACK_KEY)
            }
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

            binding.trackNameAp.text = track.trackName ?: getString(R.string.nothing_found_message)
            binding.artistNameAp.text = track.artistName ?: getString(R.string.nothing_found_message)
            binding.tvRightDuration.text = track.trackTimeFormat() ?: getString(R.string.nothing_found_message)

            if (track.collectionName != null) {
                binding.tvRightAlbumName.text = track.collectionName
            } else {
                binding.tvRightAlbumName.isGone = true
                binding.tvLeftAlbumName.isGone = true
            }

            binding.tvRightTrackYear.text = track.getTrackYear()
            binding.tvRightTrackGenre.text = track.primaryGenreName
            binding.tvRightTrackCountry.text = track.country

        } else {
            finish()
        }
    }

}