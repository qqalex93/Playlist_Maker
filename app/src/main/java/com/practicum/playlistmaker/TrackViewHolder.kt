package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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
            .load(model.artworkUrl100)
            .transform(RoundedCorners(cornerRadius))
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(albumCover)
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
    }
}