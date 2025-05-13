package com.practicum.playlistmaker.player.presentation.mapper

import com.practicum.playlistmaker.player.presentation.model.PlayerTrackInfo
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

object PlayerPresenterTrackMapper {
    fun map(track: Track?): PlayerTrackInfo {
        return PlayerTrackInfo(
            track?.trackId ?: DEFAULT_ID,
            track?.trackName ?: DEFAULT_CONTENT,
            track?.artistName ?: DEFAULT_CONTENT,
            getTrackTimeToString(track?.trackTime) ?: DEFAULT_CONTENT,
            getCoverAtWork(track?.artworkUrl100) ?: DEFAULT_LINK,
            track?.collectionName,
            getTrackYear(track?.releaseDate) ?: DEFAULT_CONTENT,
            track?.primaryGenreName ?: DEFAULT_CONTENT,
            track?.country ?: DEFAULT_CONTENT,
            track?.previewUrl ?: DEFAULT_LINK,
        )
    }

    private fun getTrackTimeToString(trackTimeMillis: Int?): String? =
        if (trackTimeMillis != null) {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
        } else {
            null
        }

    private fun getCoverAtWork(artworkUrl100: String?) =
        artworkUrl100?.replaceAfterLast("/", "512x512bb.jpg")

    private fun getTrackYear(releaseDate: String?): String? {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = releaseDate?.let { parser.parse(it) } ?: return null
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        return yearFormat.format(date)
    }

    private const val DEFAULT_CONTENT = "no result"
    private const val DEFAULT_ID = -1
    private const val DEFAULT_LINK = ""
}