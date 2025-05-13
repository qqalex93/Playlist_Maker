package com.practicum.playlistmaker.search.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.model.SearchTrackInfo

class TrackAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    private val tracks: MutableList<SearchTrackInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onClickListener.onClick(tracks[position])
        }
    }

    fun clearTracks() {
        tracks.clear()
        this.notifyDataSetChanged()
    }

    fun updateTracks(newTracks: List<SearchTrackInfo>) {
        tracks.clear()
        tracks.addAll(newTracks)
        this.notifyDataSetChanged()
    }

    fun interface OnClickListener {
        fun onClick(track: SearchTrackInfo)
    }
}


