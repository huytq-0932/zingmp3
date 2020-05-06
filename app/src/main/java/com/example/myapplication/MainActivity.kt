package com.example.myapplication

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val mediaPlayer by lazy {
        MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            isLooping = true
        }
    }

    private var currentPosition = 0
    private var currentTime = 0
    private val streams = listOf(
        "https://api.soundcloud.com/tracks/329471802/stream?client_id=7c8ae3eed403b61716254856c4155475",
        "https://api.soundcloud.com/tracks/126938662/stream/?client_id=7c8ae3eed403b61716254856c4155475",
        "https://api.soundcloud.com/tracks/252250844/stream/?client_id=7c8ae3eed403b61716254856c4155475",
        "https://api.soundcloud.com/tracks/329471802/stream?client_id=7c8ae3eed403b61716254856c4155475",
        "https://api.soundcloud.com/tracks/126938662/stream/?client_id=7c8ae3eed403b61716254856c4155475",
        "https://api.soundcloud.com/tracks/252250844/stream/?client_id=7c8ae3eed403b61716254856c4155475"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previous.setOnClickListener(this)
        next.setOnClickListener(this)
        play.setOnClickListener(this)

        playMusic(streams[currentPosition])
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.next -> next()
            R.id.back -> back()
            R.id.play -> if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                currentTime = mediaPlayer.currentPosition
                play.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            } else {
                mediaPlayer.start()
                mediaPlayer.seekTo(currentTime)
                play.setImageResource(R.drawable.ic_pause)
            }
        }
    }

    private fun next() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        playMusic(streams[(++currentPosition) % streams.size])
    }

    private fun back() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        playMusic(streams[(--currentPosition + streams.size) % streams.size])
    }

    private fun playMusic(stream: String) {
        mediaPlayer.apply {
            setDataSource(stream)
            setOnPreparedListener {
                start()
            }
            prepare()
        }
    }
}
