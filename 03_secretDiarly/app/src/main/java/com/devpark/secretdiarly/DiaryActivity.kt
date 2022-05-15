package com.devpark.secretdiarly

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {


    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText = findViewById<EditText>(R.id.et_diary)
        val detailPreferences = getSharedPreferences("DIARY", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("DETAIL", ""))

        //handler에서 runnable 쓰레드 실행
        val runnable = Runnable {
            //edit 람다는 ktx 실행
            getSharedPreferences("DIARY", Context.MODE_PRIVATE).edit {
                putString("DETAIL", diaryEditText.text.toString())
            }
        }

        //
        diaryEditText.addTextChangedListener {
            handler.removeCallbacks(runnable) // 이미 실행 중인 runnable 쓰레드가 있다면 삭제
            handler.postDelayed(runnable, 500) // 0.5초 후에 runnable 쓰레드 실행

        }
    }//end of onCreate()

}//end of class