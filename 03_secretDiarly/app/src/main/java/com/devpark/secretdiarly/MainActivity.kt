package com.devpark.secretdiarly

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    //lazy 함수는 객체가 실행할 때 초기화 진행
    private val np1: NumberPicker by lazy {
        // apply 범위 지정함수 람다식으로 초기화 진행
        findViewById<NumberPicker>(R.id.np1).apply {
            minValue = 0
            maxValue = 9
        }

    }
    private val np2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.np2).apply {
            minValue = 0
            maxValue = 9
        }
    }
    private val np3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.np3).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val open: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.btn_open)
    }

    private val changePW: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.btn_ch_pwd)
    }

    private var chPwMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //lazy 함수로 초기화를 해주었기 때문에 onCreate 에서 호출를 해주어야 초기화가 진행됨
        np1
        np2
        np3

        open.setOnClickListener {

            if (chPwMode) {
                Toast.makeText(this, "비밀번호 변경 중", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val pwPreferences = getSharedPreferences("PASSWORD", Context.MODE_PRIVATE)
            val pwFormUser = "${np1.value}${np2.value}${np3.value}"

            if (pwPreferences.getString("PASSWORD", "000").equals(pwFormUser)) {
                // 패스워드 성공
                //TODO 다이어리 페이지 작성 후에 넘겨주어야함
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                //실패
                showErrorAlert()
            }//end of if ~ else


        }//end of open btn

        changePW.setOnClickListener {
            val pwPreferences = getSharedPreferences("PASSWORD", Context.MODE_PRIVATE)
            val pwFormUser = "${np1.value}${np2.value}${np3.value}"

            if (chPwMode) {
                //번호 저장하기능 기능
                //SHaredPreferences에서 마지막 저장시 commit() 동기식, apply는 비동기식으로 저장
                //default는 apply() 비동기
                pwPreferences.edit(true) {
                    putString("PASSWORD", pwFormUser)
                }

                chPwMode = false
                changePW.setBackgroundColor(Color.BLACK)
            } else {

                if (pwPreferences.getString("PASSWORD", "000").equals(pwFormUser)) {
                    chPwMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요!", Toast.LENGTH_SHORT).show()
                    changePW.setBackgroundColor(Color.RED)

                } else {
                    //실패
                    showErrorAlert()
                }//end of if ~ else

            }
        }//end of changePW

    }//end of onCreate()

    private fun showErrorAlert() {
        AlertDialog.Builder(this)
            .setTitle("실패")
            .setMessage("비밀번호가 잘못되었습니다!")
            .setPositiveButton("확인") { dialog, whith -> }
            .setNegativeButton("테스트") { dialog, whith -> }.create().show()

    }
}