package com.example.sixthapplication

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var player: MediaPlayer
    private var timeLeft = 0L
    private val defaultTime = 1000L * 3 * 1
    private val maxTime = 1000L * 60 * 60 - 1000

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            timeLeft = millisUntilFinished
            draw()
        }

        override fun onFinish() {
            timerText.text = "0:00"
            player.start()
        }
    }

    private fun convertTime(millisUntilFinished: Long): String {
        val minuteLeft = millisUntilFinished / 1000L / 60L
        val secondLeft = millisUntilFinished / 1000L % 60L
        return "%1d:%2$02d".format(minuteLeft, secondLeft)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeLeft = defaultTime
        var timer = MyCountDownTimer(timeLeft, 100)
        draw()

        startButton.setOnClickListener {
            timer = MyCountDownTimer(timeLeft, 100)
            timer.start()
        }

        pauseButton.setOnClickListener {
            timer.cancel()
        }

        clearButton.setOnClickListener {
            timer.cancel()
            timeLeft = defaultTime
            draw()
            player.pause()
        }

        addButton.setOnClickListener {
            timeLeft += 1000
            if (timeLeft > maxTime) {
                timeLeft = maxTime
            }
            draw()
        }

        subtractButton.setOnClickListener {
            timeLeft -= 1000
            if (timeLeft < 0L) {
                timeLeft = 0
            }
            draw()
        }

        player = MediaPlayer.create(this, R.raw.bellsound)
        player.isLooping = true
    }

    private fun draw() {
        timerText.text = convertTime(timeLeft)
    }
}
