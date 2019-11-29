package com.example.sixthapplication

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundResId = 0
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
            soundPool.play(soundResId, 1.0f, 100f, 0, 0, 1.0f)
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
    }

    private fun draw() {
        timerText.text = convertTime(timeLeft)
    }

    override fun onResume() {
        super.onResume()
        soundPool =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                @Suppress("DEPRECATION")
                SoundPool(2, AudioManager.STREAM_ALARM, 0)
            } else {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build()
            }
        soundResId = soundPool.load(this, R.raw.bellsound, 1)
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }
}
