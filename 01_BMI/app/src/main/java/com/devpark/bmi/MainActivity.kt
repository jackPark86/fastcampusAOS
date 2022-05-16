package com.devpark.bmi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etHeight: EditText = findViewById(R.id.et_height)
        val etWeight = findViewById<EditText>(R.id.et_weight)

        val btnResult: Button = findViewById(R.id.btn_result)

        btnResult.setOnClickListener {

            if (etHeight.text.isEmpty() || etWeight.text.isEmpty()) {
                Toast.makeText(this, "빈 값이 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val height: Int = etHeight.text.toString().toInt()
            val weight: Int = etWeight.text.toString().toInt()

            val i = Intent(this, ResultActivity::class.java)
            i.putExtra("HEIGHT", height)
            i.putExtra("WEIGHT", weight)
            startActivity(i)

        }//end of setOnClickListener

    }//end of onCreate()

}//end of class