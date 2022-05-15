package com.devpark.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.widget.TextView
import kotlin.math.pow

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvResult: TextView = findViewById(R.id.tv_result)

        val height = intent.getIntExtra("HEIGHT", 0)
        val weight = intent.getIntExtra("WEIGHT", 0)

        // mbi 측정은 미터 단위
        val bmi = weight / (height / 100.0).pow(2.0)

        val resultTxt = when {
            bmi >= 35.0 -> "고도 비만"
            bmi >= 30.0 -> "중정도 비만"
            bmi >= 25.0 -> "경도 비만"
            bmi >= 23.0 -> "과체중"
            bmi >= 18.5 -> "정상체중"
            else -> "저체중"
        }

        tvResult.text = " Bmi : $bmi \n $resultTxt"
    }
}