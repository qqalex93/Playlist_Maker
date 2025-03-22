package com.practicum.playlistmaker

import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Int?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Int?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
) : Parcelable {
    fun getCoverAtWork() = artworkUrl100?.replaceAfterLast("/", "512x512bb.jpg")

    fun trackTimeFormat(): String? = if (trackTimeMillis != null) {
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    } else {
        null
    }

    fun getTrackYear(): String? {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = releaseDate?.let { parser.parse(it) } ?: return null
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        return yearFormat.format(date)
    }
}