package com.devpark.firebasepush

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    private val firebaseTokenTextView: TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        updateResult()
    }

    /**
     * Intent addFlag를 FLAG_ACTIVITY_SINGLE_TOP으로 주었을 때
     * B Activity를 실행 시키면 onCreate를 실행시키지 않고
     * onNewIntent 메서드를 실행 시킨
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("onNewIntent", "onNewIntent Enter!!")

        /**
         * onCreate()에서 가져온 intent가 있기 때문에
         * setIntent로 통해서 새로 들어온 intent로 교체를 해야 한다.
         */
        setIntent(intent)

        updateResult(true)
    }


    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseTokenTextView.text = task.result
                Log.d("FirebaseToken", task.result)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false) {
        Log.d("updateResult", "updateResult Enter!!")
        resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런처") + if (isNewIntent) {
            "(으)로 갱신했습니다."
        } else {
            "(으)로 실행했습니다."
        }

    }

}