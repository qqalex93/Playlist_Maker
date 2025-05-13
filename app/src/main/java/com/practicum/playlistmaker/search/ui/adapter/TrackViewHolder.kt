package com.practicum.playlistmaker.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.model.SearchTrackInfo

class TrackViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.track, parent, false)
) {
    private val albumCover: ImageView = itemView.findViewById(R.id.album_cover)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(model: SearchTrackInfo) {

        val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius_2)

        Glide.with(itemView)
            .load(model.artworkUrl)
            .transform(RoundedCorners(cornerRadius))
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(albumCover)

        trackName.text = model.trackName
        artistName.text = model.artistName
        artistName.requestLayout()
        trackTime.text = model.trackTime
    }
}