package com.devpark.lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.size

class MainActivity : AppCompatActivity() {

    private val btnClear: Button by lazy {
        findViewById(R.id.btn_clear)
    }

    private val btnAdd: Button by lazy {
        findViewById(R.id.btn_add)
    }

    private val btnRun: Button by lazy {
        findViewById(R.id.btn_run)
    }

    private val np: NumberPicker by lazy {
        findViewById(R.id.np)
    }

    private val tvList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_01),
            findViewById(R.id.tv_02),
            findViewById(R.id.tv_03),
            findViewById(R.id.tv_04),
            findViewById(R.id.tv_05),
            findViewById(R.id.tv_06)
        )
    }

    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        np.minValue = 1
        np.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }//end of onCreate


    private fun initRunButton() {
        btnRun.setOnClickListener {
            val list = getRandomNumber()
            didRun = true
            list.forEachIndexed { index, nubmer ->
                val textView = tvList[index]
                textView.text = nubmer.toString()
                textView.isVisible = true

                setNumberBackground(nubmer, textView)


            }
            Log.d("MainActivity", list.toString())
        }
    }


    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (pickNumberSet.contains(i)) {
                    continue
                }
                this.add(i)
            }
        }
        numberList.shuffle()

        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)
        return newList.sorted()
    }


    private fun initAddButton() {
        btnAdd.setOnClickListener {
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(np.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = tvList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = np.value.toString()
            setNumberBackground(np.value, textView)


            pickNumberSet.add(np.value)
        }
    }

    private fun setNumberBackground(number: Int, textView: TextView) {
        when (number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    private fun initClearButton() {
        btnClear.setOnClickListener {
            pickNumberSet.clear()
            tvList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }
}//end of class