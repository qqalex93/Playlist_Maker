package com.practicum.playlistmaker.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.track, parent, false)
) {
    private val albumCover: ImageView = itemView.findViewById(R.id.album_cover)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(model: Track) {

        val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius_2)

        Glide.with(itemView)
            .load(model.artworkUrl100 ?: "")
            .transform(RoundedCorners(cornerRadius))
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(albumCover)

        trackName.text = model.trackName ?: itemView.context.getString(R.string.nothing_found_message)
        artistName.text = model.artistName ?: itemView.context.getString(R.string.nothing_found_message)
        artistName.requestLayout()
        trackTime.text = model.trackTimeMillis ?: itemView.context.getString(R.string.nothing_found_message)
    }
}