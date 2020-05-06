package com.example.myapplication

import android.media.MediaMetadataRetriever

data class Song(
    val title: String = "",
    val artist: String = "",
    val duration: String = ""
) {
    constructor(metaData: MediaMetadataRetriever) : this(
        title = metaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
        artist = metaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
        duration = metaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
    )
}