package com.devpark.gallery

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()
    private var currentPosition = 0
    private var timer: Timer? = null

    private val frontImg: ImageView by lazy {
        findViewById<ImageView>(R.id.imageFront)
    }

    private val backImg: ImageView by lazy {
        findViewById<ImageView>(R.id.ImageBack)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)
        supportActionBar?.hide()

        getPhotoUriFromIntent()
        //startTimer()

    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }


    private fun startTimer() {
        timer = timer(period = 5 * 1000) {
            runOnUiThread {
                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1
                backImg.setImageURI(photoList[current])
                frontImg.alpha = 0f
                frontImg.setImageURI(photoList[next])
                frontImg.animate().alpha(1.0f).setDuration(1000).start()

                currentPosition = next
            }
        }
    }//end of startTimer

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}