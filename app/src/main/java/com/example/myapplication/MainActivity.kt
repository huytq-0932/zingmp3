package com.example.myapplication

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val songs = listOf(R.raw.first, R.raw.second, R.raw.third)
    private var mediaPlayer = MediaPlayer()
    private var currentSongIndex = 0
    private var currentSongTimePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        startPlay(songs[currentSongIndex])
    }

    private fun initViews() {
        previous.setOnClickListener(this)
        next.setOnClickListener(this)
        play.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.next -> next()
            R.id.back -> back()
            R.id.play -> if (mediaPlayer.isPlaying) pause() else playContinue()
        }
    }

    private fun next() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        startPlay(songs[(++currentSongIndex) % songs.size])
    }

    private fun back() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        startPlay(songs[(--currentSongIndex + songs.size) % songs.size])
    }

    private fun startPlay(songResId: Int) {
        showSongInformation(songResId)
        mediaPlayer = MediaPlayer.create(this, songResId)
        mediaPlayer.start()
        setProgress()
    }

    private fun pause() {
        mediaPlayer.pause()
        currentSongTimePosition = mediaPlayer.currentPosition
        play.setImageResource(R.drawable.ic_play_arrow_black_24dp)
    }

    private fun playContinue() {
        startPlay(songs[currentSongIndex])
        mediaPlayer.seekTo(currentSongTimePosition)
        play.setImageResource(R.drawable.ic_pause)
    }

    private fun showSongInformation(resId: Int) {
        val mediaPath = Uri.parse("android.resource://$packageName/$resId")
        val metadata = MediaMetadataRetriever().apply {
            setDataSource(this@MainActivity, mediaPath)
        }
        val songDuration =
            metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
        duration.text = songDuration.toTime()

        val songTitle = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        textTrackName.text = songTitle

        val songArtist = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        textArtistName.text = songArtist

    }

    private fun setProgress() {
        seekTime.max = mediaPlayer.duration
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay({
            seekTime.progress = mediaPlayer.currentPosition
        }, 1, 1, TimeUnit.SECONDS)
    }

    private fun Int.toTime() = "${this / 60000}:${(this / 1000) % 60}"
}
